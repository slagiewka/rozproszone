package lab4.Locators;

import Ice.*;
import Ice.Object;
import lab4.Impl.DemoAppImpl;
import lab4.Persistance.DemoAppEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class K1ServantLocator implements ServantLocator {
    private static final Logger LOGGER = LoggerFactory.getLogger(K1ServantLocator.class);
    private final ObjectAdapter adapter;
    private final DemoAppEntity entity;

    public K1ServantLocator(ObjectAdapter adapter) {
        this.adapter = adapter;
        this.entity = new DemoAppEntity();
    }

    @Override
    public Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        int numId;
        try {
            numId = Integer.parseInt(current.id.name);
            if (numId < 0 || numId >= 100)
                return null;
        } catch (NumberFormatException ex) {
            return null;
        }

        DemoAppImpl lazyServant;
        LOGGER.info("Missing servant #" + current.id.name);
        lazyServant = entity.loadSerial(numId);
        adapter.add(lazyServant, current.id);

        LOGGER.info("Locating lazy servant #" + lazyServant.getId());
        return lazyServant;
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {}

    @Override
    public void deactivate(String s) {}
}
