package lab4.Locators;

import Ice.*;
import Ice.Object;
import lab4.Impl.DemoAppImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class K2ServantLocator implements ServantLocator {
    private static final Logger LOGGER = LoggerFactory.getLogger(K2ServantLocator.class);

    @Override
    public Object locate(Current current, LocalObjectHolder localObjectHolder) throws UserException {
        DemoAppImpl servant = new DemoAppImpl();
        LOGGER.info("Creating prototype servant: #" + servant.getId());
        return servant;
    }

    @Override
    public void finished(Current current, Object object, java.lang.Object o) throws UserException {

    }

    @Override
    public void deactivate(String s) {

    }
}
