package lab6;

import lab6.watchers.ChildrenWatcher;
import lab6.watchers.TaskCoordinator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

class Executor implements Runnable, Watcher {
    private final ZooKeeper zooKeeper;
    private final String znode;
    private TaskCoordinator taskCoordinator;
    private ChildrenWatcher childrenWatcher;

    Executor(
            String znode,
            ZooKeeper zooKeeper,
            String exec[],
            Runtime runtime
    ) throws KeeperException, IOException {
        this.znode = znode;
        this.zooKeeper = zooKeeper;
        zooKeeper.register(this);
        taskCoordinator = new TaskCoordinator(znode, exec, runtime);
        childrenWatcher = new ChildrenWatcher(znode);
        zooKeeper.exists(znode, true, null, this);
    }

    public void run() {
        while (true) {
        }
    }

    public void process(WatchedEvent event) {
        taskCoordinator.process(event);
        zooKeeper.exists(znode, true, null, this);
        zooKeeper.getChildren(znode, true, childrenWatcher, this);
    }
}
