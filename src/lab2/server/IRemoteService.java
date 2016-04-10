//package lab3.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteService extends Remote {
    void registerPlayer(Player player) throws RemoteException;
//    void makeMove(Player player, Move move) throws RemoteException;
    void chooseGame(Player player, String mode) throws RemoteException;
}
