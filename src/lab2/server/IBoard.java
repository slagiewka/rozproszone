//package lab3.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IBoard extends Remote {
    boolean movePossible(Coordinates coordinates) throws RemoteException;

    void makeMovement(Movement movement) throws RemoteException;

    boolean thereIsAWinner() throws RemoteException;

    List<Coordinates> unmarkedCells() throws RemoteException;
}
