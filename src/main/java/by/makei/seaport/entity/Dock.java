package by.makei.seaport.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Dock {
    private static final Logger logger = LogManager.getLogger();
    private int dockId;
    private Port port;
    private Ship ship;
    ReentrantLock locker;
    Condition condition;


    public Dock(int dockId, Port port) {
        this.port = port;
        this.dockId = dockId;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
        locker = new ReentrantLock(); // создаем блокировку
        condition = locker.newCondition(); // получаем условие, связанное с блокировкой
    }

    public void unLoadShip() {
//        locker.lock();
        try {
            while (ship.isContainerExist()) {
                if (port.getContainersNumber().doubleValue() < port.getMaxContainersNumber()) {
                    ship.decrementContainer();
                    port.incrementContainer();
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        logger.log(Level.ERROR, "something go wrong", e);
                    }
                } else {
                    Thread.yield();
                }
            }
        } finally {
//            locker.unlock();
        }
    }

    public void loadShip() {
//        locker.lock();
        try {
            while (ship.isFreeSpace()) {
                if (port.getContainersNumber().doubleValue() > 0) {
                    port.decrementContainer();
                    ship.incrementContainer();
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);

                    } catch (InterruptedException e) {
                        logger.log(Level.ERROR, "something go wrong", e);
                    }
                } else {
                    Thread.yield();
                }
            }
        } finally {
//            locker.unlock();
        }
    }
}
