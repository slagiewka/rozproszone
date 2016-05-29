package lab6;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        if (args.length < 4) {
            System.err.println("USAGE: Main connectionString program [args ...]");
            System.exit(2);
        }
        String connectionString = args[0];
        String exec[] = new String[args.length - 1];
        final String znode = "/znode_testowy";
        System.arraycopy(args, 1, exec, 0, exec.length);
        final ZooKeeper zooKeeper = new ZooKeeper(connectionString, 3000, null);

        new Thread(new Executor(znode, zooKeeper, exec, Runtime.getRuntime())).start();
    }
}
