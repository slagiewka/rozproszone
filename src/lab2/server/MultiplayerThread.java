//package lab3.server;

import java.rmi.RemoteException;
import java.util.Queue;

public class MultiplayerThread implements Runnable {
    private final Player player;
    private final Queue<Player> queue;

    public MultiplayerThread(Player player, Queue<Player> queue) {
        this.player = player;
        this.queue = queue;
    }

    @Override
    public void run() {
        Player secondPlayer = null;

        synchronized (queue) {
            if (queue.isEmpty()) {
                queue.add(player);
                notifyPlayerWaiting();
            } else {
                secondPlayer = queue.remove();
            }
        }

        if (secondPlayer != null) {
            try {
                startGame(secondPlayer);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyPlayerWaiting() {
        player.onWaiting();
    }

    private void startGame(Player secondPlayer) throws RemoteException {
        new Coordinator(player, secondPlayer, new Board()).coordinate();
    }
}
