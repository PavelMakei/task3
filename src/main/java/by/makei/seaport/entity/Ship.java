package by.makei.seaport.entity;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;


public class Ship extends Thread {
    private static Logger logger = LogManager.getLogger();
    private String id;
    private int containersMaxNumber;
    private int containerExists;
    private Dock dock;
    Port port;

    public Ship(@NotNull String name, Port port, int containersMaxNumber, int containerExists) {
        super("Ship - " + name);
        id = name;
        this.port = port;
        this.containersMaxNumber = containersMaxNumber;
        this.containerExists = containerExists;
    }

    public void setContainerExists(int containerExists) {
        this.containerExists = containerExists;
    }

    public boolean isContainerExist() {
        return containerExists > 0;
    }

    public boolean isFreeSpace() {
        return containersMaxNumber > containerExists;
    }

    public void popContainer() {
        containerExists--;
    }

    public void pushContainer() {
        containerExists++;
    }


    @Override
    public void run() {
        dock = port.popDockPool();
        dock.setShip(this);
        if (containerExists != 0) {
            dock.unLoadShip();
        } else {
            dock.loadShip();
        }
        port.pushDockPool(dock);
        dock.setShip(null);
    }
}
