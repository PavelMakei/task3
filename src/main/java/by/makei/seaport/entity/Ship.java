package by.makei.seaport.entity;

import by.makei.seaport.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


public class Ship extends Thread {
    private static final Logger logger = LogManager.getLogger();
    private final String shipName;
    private final int containersMaxNumber;
    private int containerExists;
    private Dock dock;
    private Port port;
    private WaterArea waterArea;

    public Ship(@NotNull String name, Port port, int containersMaxNumber, int containerExists) {
        super("Ship - " + name);
        this.shipName = name;
        this.port = port;
        this.containersMaxNumber = containersMaxNumber;
        this.containerExists = containerExists;
        this.waterArea = WaterArea.getInstance();

    }

    public boolean isContainerExist() {
        return containerExists > 0;
    }

    public boolean isFreeSpace() {
        return containersMaxNumber > containerExists;
    }

    public void decrementContainer() {
        containerExists--;
    }

    public void incrementContainer() {
        containerExists++;
    }

    public String getShipName() {
        return shipName;
    }

    @Override
    public void run() {
        port.incrementShipsCounter();


        try {
            waterArea.getIntoWaterArea();
            dock = port.popDockPool();
        } catch (CustomException e) {
            logger.log(Level.ERROR, "can't get dock", e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Process was interrupted {}",Thread.currentThread().getName(), e);
        }
        dock.setShip(this);

        if (containerExists != 0) {
            dock.unLoadShip();
        } else {
            dock.loadShip();
        }
        try {
            dock.setShip(null);
            port.pushDockPool(dock);
            waterArea.getOutWaterArea();
        } catch (CustomException e) {
            logger.log(Level.ERROR, "can't return dock", e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Process was interrupted {}",Thread.currentThread().getName(), e);
        }
        port.decrementShipsCounter();
    }
}
