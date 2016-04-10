//package lab3.server;

import java.rmi.RemoteException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class RemoteService implements IRemoteService {
    private final int QUEUE_SIZE = 2;
    private final Queue<Player> playerQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
    private final Set<String> registeredNames = ConcurrentHashMap.newKeySet();

    @Override
    public void registerPlayer(Player player) throws RemoteException {
        synchronized (registeredNames) {
            if (registeredNames.contains(player.getNick())) {
                    throw new UserAlreadyRegisteredException();
            }

            registeredNames.add(player.getNick());
        }
    }

    @Override
    public void chooseGame(Player player, String mode) throws RemoteException {
        switch (mode) {
            case "multi":
                new Thread(new MultiplayerThread(player, playerQueue)).start();
                break;
            case "single":
                new Thread(new SingleplayerThread(player)).start();
                break;
            default:
                throw new RemoteException("As a mode choose multiplayer or singleplayer");
        }
    }

    public class UserAlreadyRegisteredException extends RemoteException {
    }
}
