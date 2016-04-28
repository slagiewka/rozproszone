package lab4.Impl;

import Ice.Current;
import lab4.app._DemoAppDisp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoAppImpl extends _DemoAppDisp {
    private final static Logger LOGGER = LoggerFactory.getLogger(DemoAppImpl.class);
    private final static AtomicInteger COUNTER = new AtomicInteger(0);

    private int id;

    public DemoAppImpl() {
        id = COUNTER.incrementAndGet();
        LOGGER.info("Creating servant #" + id);
    }

    public DemoAppImpl(int id) {
        this.id = id;
        LOGGER.info("Creating servant #" + id + " (existing)");
    }

    @Override
    public String demo(int i, Current __current) {
        LOGGER.info("Demo is being run by " + i);

        return String.valueOf(id);
    }

    @Override
    public int getId(Current __current) {
        return id;
    }
}
