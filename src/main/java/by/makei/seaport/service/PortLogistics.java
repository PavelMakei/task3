package by.makei.seaport.service;

import by.makei.seaport.entity.Port;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortLogistics extends Thread{
    private static final Logger logger = LogManager.getLogger();
    private Port port;

    public PortLogistics(Port port) {
        this.port = port;
    }

    @Override
    public void run (){
        logger.log(Level.INFO, "Port logistics started");
        int maxContainersNumber = (int)port.getMaxContainersNumber();
        int numberContainersTopLimit = (int) (maxContainersNumber * port.getContainerMaxLoadFactor());
        int numberContainersBottomLimit = (int) (maxContainersNumber * port.getContainerMinLoadFactor());
        int count = 0;

        if(port.getContainersNumber().intValue() > numberContainersTopLimit) {
            while (port.getContainersNumber().intValue() > maxContainersNumber/2){
                port.decrementContainer();
                count++;
            }
            logger.log(Level.INFO, "Added {} containers by logistics", count);
        }else if (port.getContainersNumber().intValue() < numberContainersBottomLimit){
            while (port.getContainersNumber().intValue() < maxContainersNumber/2){
                port.incrementContainer();
                count++;
            }
            logger.log(Level.INFO, "Removed {} containers by logistics", count);
        }
    }

}
