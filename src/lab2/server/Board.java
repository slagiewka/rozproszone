//package lab3.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Board extends UnicastRemoteObject implements IBoard {
    private final BoardCell[][] board = new BoardCell[3][3];
    private final WinnerFinder winnerFinder = new WinnerFinder(
            board, 3, 3, 3
    );

    public Board() throws RemoteException {
        clearBoard();
    }

    private void clearBoard() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board[i][j] = BoardCell.EMPTY;
            }
        }
    }

    @Override
    public boolean movePossible(Coordinates coordinates) {
        return !eachCellMarked() && !thereIsAWinner() && unmarkedCells().contains(coordinates);
    }

    @Override
    public void makeMovement(Movement aMovement) {
        RemoteClient.errorHandler(movePossible(aMovement.getCoordinates()), "Move not possible!");

        board[aMovement.getCoordinates().getRow()][aMovement.getCoordinates().getColumn()]
                = aMovement.getMarker();
    }


    @Override
    public boolean thereIsAWinner() {
        return thereIsAWinningRow() || thereIsAWinningColumn() || thereIsAWinningLeftDiagonal()
                || thereIsAWinningRightDiagonal();
    }

    @Override
    public List<Coordinates> unmarkedCells(){
        List<Coordinates> unmarkedCells = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == BoardCell.EMPTY) {
                    unmarkedCells.add(
                            new Coordinates(i, j)
                    );
                }
            }
        }

        return unmarkedCells;
    }

    private boolean eachCellMarked() {
        return unmarkedCells().isEmpty();
    }

    private boolean thereIsAWinningRow() {
        return winnerFinder.thereIsAWinningRow();
    }

    private boolean thereIsAWinningRightDiagonal() {
        return winnerFinder.thereIsAWinningRightDiagonal();
    }

    private boolean thereIsAWinningLeftDiagonal() {
        return winnerFinder.thereIsAWinningLeftDiagonal();
    }

    private boolean thereIsAWinningColumn() {
        return winnerFinder.thereIsAWinningColumn();
    }

    @Override
    public String toString() {
        String boardStringRepresentation = "";

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                boardStringRepresentation += board[i][j] + " ";
            }
            boardStringRepresentation += "\n";
        }

        return boardStringRepresentation;
    }
}
