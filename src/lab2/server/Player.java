//package lab3.server;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Player implements IPlayer {
    private final String nick;

    public Player(String nick) {
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    @Override
    public void doMove(IBoard board, BoardCell marker) throws RemoteException {
        final Scanner stdIn = new Scanner(System.in);

        int x, y;
        Coordinates coordinates;

        do {
            System.out.println("Please type cell coords (row, column):\n");
            x = stdIn.nextInt();
            y = stdIn.nextInt();

        } while (!coordsValid(x, y) || !board.movePossible(new Coordinates(x, y)));

        board.makeMovement(
                new Movement(new Coordinates(x, y), marker)
        );
    }

    @Override
    public void onWin() throws RemoteException {
        System.out.println("You won!");
    }

    @Override
    public void onLoss() throws RemoteException {
        System.out.println("You lost…");
    }

    @Override
    public void onDraw() throws RemoteException {
        System.out.println("It's a draw!");
    }

    @Override
    public void onWaiting() {
        System.out.println("You're first to arrive at the game field… stay tuned!");
    }

    @Override
    public void onGameStarted(String nick) {
        System.out.println("Started game with " + nick + "\n");
    }

    @Override
    public void onBoardChanged(String boardRepresentation) throws RemoteException {
        System.out.println(boardRepresentation);
    }

    private boolean coordsValid(int row, int column) {
        return row >= 0 && row < 3 && column >= 0 && column < 3;
    }
}
