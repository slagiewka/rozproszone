//package lab3.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) throws MalformedURLException, RemoteException {
        IRemoteService serviceImpl = new RemoteService();
        IRemoteService serviceStub = (IRemoteService) UnicastRemoteObject.exportObject(serviceImpl, 0);
        Naming.rebind("rmi://localhost:1099/tictactoe", serviceStub);
    }
}
