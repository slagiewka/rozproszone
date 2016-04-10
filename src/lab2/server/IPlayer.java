//package lab3.server;

import java.rmi.RemoteException;

public interface IPlayer {
    void doMove(IBoard board, BoardCell marker) throws RemoteException;

    default void onWin() throws RemoteException {
    }

    default void onLoss() throws RemoteException {

    }

    default void onDraw() throws RemoteException {

    }

    default void onWaiting() throws RemoteException {

    }

    default void onGameStarted(String enemyNick) throws RemoteException {

    }

    default void onBoardChanged(String boardRepresentation) throws RemoteException {
    }
}
