package by.makei.seaport.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeUnit;

public class Dock {
    private static final Logger logger = LogManager.getLogger();
    private int dockId;
    private Port port;
    private Ship ship;

    public Dock(int dockId, Port port) {
        this.port = port;
        this.dockId = dockId;
    }

    public int getDockId() {
        return dockId;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void unLoadShip() {

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
    }

    public void loadShip() {
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
    }
}

