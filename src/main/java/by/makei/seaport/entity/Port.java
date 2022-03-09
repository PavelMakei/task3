package by.makei.seaport.entity;

import by.makei.seaport.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Port {
    private static final Logger logger = LogManager.getLogger();
    private static final AtomicReference<Port> instance = new AtomicReference<>();

    private double maxContainersNumber;
    private double docksNumber;
    private double containerMaxLoadFactor;
    private double containerMinLoadFactor;
    private AtomicInteger containersNumber;
    private ArrayDeque<Dock> dockPool;
    private AtomicInteger debit;
    private AtomicInteger credit;
    private AtomicInteger shipCounter;
    private final Lock locker = new ReentrantLock();
    private final Condition onGetDock = locker.newCondition();
    private final Condition onReturnDock = locker.newCondition();
    private final AtomicInteger dockGetCount = new AtomicInteger(0);
    private final AtomicInteger dockReturnCount = new AtomicInteger(0);


    private Port() {}

    public static Port getInstance() { //locking by compare and set
        while (true) {
            Port current = instance.get();
            if (current != null) {
                return current;
            }
            current = new Port();
            if (instance.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    public double getMaxContainersNumber() {
        return maxContainersNumber;
    }

    public void setMaxContainersNumber(double maxContainersNumber) {
        this.maxContainersNumber = maxContainersNumber;
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

    public AtomicInteger getDebit() {
        return debit;
    }

    public AtomicInteger getCredit() {
        return credit;
    }

    public AtomicInteger getDockGetCount() {
        return dockGetCount;
    }

    public AtomicInteger getDockReturnCount() {
        return dockReturnCount;
    }


    public void initialise() {
        debit = new AtomicInteger(0);
        credit = new AtomicInteger(0);
        dockPool = new ArrayDeque<>();
        shipCounter = new AtomicInteger(0);

        for (int i = 0; i < docksNumber; i++) {
            dockPool.push(new Dock(i, this));
        }
    }

    public Dock popDockPool() throws CustomException {
        locker.lock();
        Dock dock;
        try {
            while (dockPool.isEmpty()) {
                onGetDock.await();
            }
            dock = dockPool.pop();
            dockGetCount.incrementAndGet();
            logger.log(Level.INFO, "thread {} get dock {},free docks - {}", Thread.currentThread().getName(), dock.getDockId(), dockPool.size());
            onReturnDock.signal();
            return dock;
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "thread {} was interrupted", Thread.currentThread().getName(), e);
        } finally {
            locker.unlock();
        }
        throw new CustomException("Dock wasn't given");
    }

    public void pushDockPool(Dock dock) throws CustomException {
        locker.lock();
        try {
            dockPool.push(dock);
            dockReturnCount.incrementAndGet();
            logger.log(Level.INFO, "thread {} return dock, free docks - {}", Thread.currentThread().getName(), dockPool.size());
            onGetDock.signal();
        } finally {
            locker.unlock();
        }
    }

    public AtomicInteger getShipCounter() {
        return shipCounter;
    }

    public void incrementContainer() {
        containersNumber.getAndIncrement();
        debit.getAndIncrement();
    }

    public void decrementContainer() {
        containersNumber.getAndDecrement();
        credit.getAndIncrement();
    }

    public void incrementShipsCounter() {
        shipCounter.getAndIncrement();
    }

    public void decrementShipsCounter() {
        shipCounter.getAndDecrement();
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

}