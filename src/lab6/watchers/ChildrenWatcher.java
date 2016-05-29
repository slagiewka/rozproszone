package lab6.watchers;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Objects;

public class ChildrenWatcher implements AsyncCallback.Children2Callback {

    private final String pathToWatch;

    public ChildrenWatcher(String pathToWatch) {
        this.pathToWatch = pathToWatch;
    }

    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        if (!pathToWatch.equals(path) || Objects.isNull(children)) {
            return;
        }
        System.out.println(String.format("Znode %s has %d children: \n%s", path, children.size(), children));
    }

}
