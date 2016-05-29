package lab6.watchers;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TaskCoordinator implements Watcher {

    private final String pathToWatch;
    private final String[] commandToExecute;
    private final Runtime runtime;

    private Process child = null;

    public TaskCoordinator(String pathToWatch, String[] exec, Runtime runtime) {
        this.pathToWatch = pathToWatch;
        this.commandToExecute = exec;
        this.runtime = runtime;
    }

    public void process(WatchedEvent event) {
        if (!pathToWatch.equals(event.getPath())) {
            return;
        }

        final Event.EventType eventType = event.getType();

        switch (eventType) {
            case NodeCreated:
                exec();
                break;
            case NodeDeleted:
                stop();
                break;
            default:
                break;
        }

    }

    private void exec() {
        if (child != null) {
            System.out.println("Child already started");
            return;
        }

        try {
            child = runtime.exec(commandToExecute);

            new StreamWriter(child.getInputStream(), System.out);
            new StreamWriter(child.getErrorStream(), System.err);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Child started");
    }

    private void stop() {
        if (child == null) {
            System.out.println("Child already gone");
            return;
        }


        child.destroy();
        try {
            child.waitFor();
            child = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Child process killed");
    }

    private static class StreamWriter extends Thread {
        OutputStream os;
        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException ignored) {
            }

        }
    }
}
