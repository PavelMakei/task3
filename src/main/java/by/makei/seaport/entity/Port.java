package by.makei.seaport.entity;

import by.makei.seaport.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private static final Logger logger = LogManager.getLogger();
    private static volatile Port instance;

    private double maxContainersNumber;
    private double docksNumber;
    private double containerMaxLoadFactor;
    private double containerMinLoadFactor;
    private AtomicInteger containersNumber;
    private ArrayDeque<Dock> dockPool;
    private AtomicInteger debit;
    private AtomicInteger credit;
    private Lock lock;

    private Port() {
    }

    public static Port getInstance()  { //double-checked locking
        if (instance == null) {
            synchronized (Port.class) {
                if (instance == null) {
                    instance = new Port();
                }
            }
        }
        return instance;
    }

    public double getMaxContainersNumber() {
        return maxContainersNumber;
    }

    public void setMaxContainersNumber(double maxContainersNumber) {
        this.maxContainersNumber = maxContainersNumber;
    }

    public double getDocksNumber() {
        return docksNumber;
    }

    public void setDocksNumber(double docksNumber) {
        this.docksNumber = docksNumber;
    }

    public double getContainerMaxLoadFactor() {
        return containerMaxLoadFactor;
    }

    public void setContainerMaxLoadFactor(double containerMaxLoadFactor) {
        this.containerMaxLoadFactor = containerMaxLoadFactor;
    }

    public double getContainerMinLoadFactor() {
        return containerMinLoadFactor;
    }

    public void setContainerMinLoadFactor(double containerMinLoadFactor) {
        this.containerMinLoadFactor = containerMinLoadFactor;
    }

    public AtomicInteger getContainersNumber() {
        return containersNumber;
    }

    public void setContainersNumber(AtomicInteger containersNumber) {
        this.containersNumber = containersNumber;
    }

    public void initialise() {

        debit = new AtomicInteger(0);
        credit = new AtomicInteger(0);
        lock = new ReentrantLock();
        dockPool = new ArrayDeque<>();
        for (int i = 0; i < docksNumber; i++) {
            dockPool.push(new Dock(i, this));
        }
    }

    public Dock popDockPool() {
        while (true) {
            if (!dockPool.isEmpty()) {
                try {
                    lock.lock();
                    if (!dockPool.isEmpty()) {
                        logger.log(Level.INFO, "thread {} get dockSize - ", Thread.currentThread().getName(), dockPool.size() );
                        return dockPool.pop();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public void pushDockPool(Dock dock) {
        while (true) {
            if (!dockPool.isEmpty()) {
                try {
                    lock.lock();
                    if (!dockPool.isEmpty()) {
                        dockPool.push(dock);
                        logger.log(Level.INFO, "thread {} return dockSize - ", Thread.currentThread().getName(), dockPool.size() );
                        return;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }



    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Port{");
        sb.append("maxContainersNumber=").append(maxContainersNumber);
        sb.append(", docksNumber=").append(docksNumber);
        sb.append(", containerMaxLoadFactor=").append(containerMaxLoadFactor);
        sb.append(", containerMinLoadFactor=").append(containerMinLoadFactor);
        sb.append(", containersNumber=").append(containersNumber.doubleValue());
        sb.append(", dock=").append(dockPool);
        sb.append(", debit=").append(debit.doubleValue());
        sb.append(", credit=").append(credit.doubleValue());
        sb.append('}');
        return sb.toString();
    }

    public void pushContainer() {
        containersNumber.getAndIncrement();
        debit.getAndIncrement();
    }

    public void popContainer() {
        containersNumber.getAndDecrement();
        credit.getAndIncrement();
    }
}