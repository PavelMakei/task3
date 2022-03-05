package by.makei.seaport.service;

import by.makei.seaport.entity.Port;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.TimerTask;

public class PortLogistics extends TimerTask {
    private static final Logger logger = LogManager.getLogger();
    private static volatile PortLogistics instance;
    private static Port port;

    private PortLogistics() {
    }

    public static PortLogistics getInstance(Port port) { //double-checked locking
        if (instance == null) {
            synchronized (Port.class) {
                if (instance == null) {
                    instance = new PortLogistics();
                    PortLogistics.port = Port.getInstance();
                }
            }
        }
        return instance;
    }

    @Override
    public void run() {
        int maxContainersNumber = (int) port.getMaxContainersNumber();
        int numberContainersTopLimit = (int) (maxContainersNumber * port.getContainerMaxLoadFactor());
        int numberContainersBottomLimit = (int) (maxContainersNumber * port.getContainerMinLoadFactor());
        int count = 0;

        logger.log(Level.DEBUG, "Logistic started work");
        if (port.getContainersNumber().intValue() > numberContainersTopLimit) {
            while (port.getContainersNumber().intValue() > maxContainersNumber / 2) {
                port.decrementContainer();
                count++;
            }
            logger.log(Level.INFO, "Added {} containers by logistics", count);
        } else if (port.getContainersNumber().intValue() < numberContainersBottomLimit) {
            while (port.getContainersNumber().intValue() < maxContainersNumber / 2) {
                port.incrementContainer();
                count++;
            }
            logger.log(Level.INFO, "Removed {} containers by logistics", count);
        }
        logger.log(Level.DEBUG, "Logistic finished work");
    }
}
