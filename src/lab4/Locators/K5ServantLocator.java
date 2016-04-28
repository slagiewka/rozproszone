package lab4.Locators;

import Ice.*;
import Ice.Object;
import lab4.Impl.DemoAppImpl;
import lab4.Persistance.DemoAppEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class K5ServantLocator implements ServantLocator {
    private final static Logger LOGGER = LoggerFactory.getLogger(K5ServantLocator.class);

    private final int maxServantsInMemory;
    private Deque<DemoAppImpl> queue = new LinkedList<>();

    private DemoAppEntity dbEntity = new DemoAppEntity();
    private Map<Integer, Integer> dbIdByAppId = new HashMap<>();

    public K5ServantLocator(int maxServantsInMemory) {
        if (maxServantsInMemory <= 0) {
            throw new IllegalArgumentException("Cache has to have at least one element");
        }
        this.maxServantsInMemory = maxServantsInMemory;
        LOGGER.info("Initializing pool of " + maxServantsInMemory + " servants");

        for (int i = 0; i < this.maxServantsInMemory; i++) {
            queue.addFirst(new DemoAppImpl(1000 + i));
        }
    }

    @Override
    public Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        int id;
        try {
            id = Integer.parseInt(current.id.name);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ex);
        }

        DemoAppImpl servant = getServantFromMemory(id);
        if (servant == null) {
            servant = loadServantFromDb(id);
            if(servant == null)
                servant = new DemoAppImpl(id);

            if(queue.size() == maxServantsInMemory)
                evictLeastRecentlyUsed();

            queue.addFirst(servant);
        }

        moveToFront(servant);

        return servant;
    }

    private DemoAppImpl getServantFromMemory(int id) {
        for (DemoAppImpl servant : queue) {
            if (servant.getId() == id) {
                return servant;
            }
        }

        return null;
    }

    private DemoAppImpl loadServantFromDb(int id) {
        if (!dbIdByAppId.containsKey(id))
            return null;
        int databaseId = dbIdByAppId.get(id);
        DemoAppImpl servant = dbEntity.load(databaseId);

        if(servant == null) {
            LOGGER.info("Not found in db: #" + id);
        } else {
            LOGGER.info("Loading from db: #" + servant.getId());
        }

        return servant;
    }

    private void moveToFront(DemoAppImpl servant) {
        queue.remove(servant);
        queue.addFirst(servant);
    }


    private void evictLeastRecentlyUsed() {
        DemoAppImpl leastRecentlyUsed = queue.removeLast();

        LOGGER.info("Evicting #" + leastRecentlyUsed.getId());

        int dbId = dbEntity.save(leastRecentlyUsed);
        dbIdByAppId.put(leastRecentlyUsed.getId(), dbId);
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {

    }

    @Override
    public void deactivate(String s) {

    }
}
