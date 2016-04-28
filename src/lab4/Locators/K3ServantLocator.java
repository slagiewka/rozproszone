package lab4.Locators;

import Ice.*;
import Ice.Object;
import lab4.Impl.DemoAppImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class K3ServantLocator implements ServantLocator {
    private static final Logger LOGGER = LoggerFactory.getLogger(K3ServantLocator.class);
    private DemoAppImpl servantsArray[];
    private int index;

    public K3ServantLocator(int size) {
        if(size <= 0)
            throw new IllegalArgumentException("Pool size has to be positive");

        LOGGER.info("Allocating "+ size + " servants");
        servantsArray = new DemoAppImpl[size];
        for (int i = 0; i < size; i++) {
            servantsArray[i] = new DemoAppImpl();
        }
        index = 0;
    }

    @Override
    public Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        DemoAppImpl servant = servantsArray[index];
        index = ++index % servantsArray.length;

        LOGGER.info("Locating servant from queue: #" + servant.getId());
        return servant;
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {

    }

    @Override
    public void deactivate(String s) {

    }
}
