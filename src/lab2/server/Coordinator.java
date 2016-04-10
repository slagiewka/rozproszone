//package lab3.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Coordinator {
    private final Player playerOne;
    private final Player playerTwo;
    private final IBoard board;
    private final Map<Player, BoardCell> markers = new HashMap<>();

    public Coordinator(Player playerOne, Player playerTwo, IBoard board) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.board = board;

        markers.put(playerOne, BoardCell.CROSS);
        markers.put(playerTwo, BoardCell.NOUGHT);
    }

    public void coordinate() throws RemoteException {
        final int maximumMoves = 3 * 3;

        playerOne.onGameStarted(playerTwo.getNick());
        playerTwo.onGameStarted(playerOne.getNick());

        for (int move = 0; move < maximumMoves; ++move) {
            final Player currentPlayer = determineCurrentPlayer(move);
            final Player waitingPlayer = determineWaitingPlayer(move);

            currentPlayer.doMove(board, markers.get(currentPlayer));

            currentPlayer.onBoardChanged(board.toString());
            waitingPlayer.onBoardChanged(board.toString());

            if (board.thereIsAWinner()) {
                currentPlayer.onWin();
                waitingPlayer.onLoss();
                return;
            }
        }

        playerOne.onDraw();
        playerTwo.onDraw();

        UnicastRemoteObject.unexportObject(board, false);
    }

    private Player determineCurrentPlayer(int move) {
        return move % 2 == 0 ? playerOne : playerTwo;
    }

    private Player determineWaitingPlayer(int move) {
        return move % 2 == 1 ? playerOne : playerTwo;
    }
}
