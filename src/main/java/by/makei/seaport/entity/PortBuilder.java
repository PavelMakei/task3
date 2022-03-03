package by.makei.seaport.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PortBuilder {
    private static final Logger logger = LogManager.getLogger();
    //set defaults
    private double maxContainersNumber = 300;
    private double docksNumber = 5;
    private double containerMaxLoadFactor = 0.75;
    private double containerMinLoadFactor = 0.25;
    private AtomicInteger containersInitNumber = new AtomicInteger(300);
    private Port port;

    public Port getPort(Map<String, Double> initMap)  {
        port = Port.getInstance();
        initMap.forEach((key, value) -> {
            switch (key) {
                case "max_containers" -> {
                    maxContainersNumber = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case "docks_number" -> {
                    docksNumber = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case "container_max_load" -> {
                    containerMaxLoadFactor = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case "container_min_load" -> {
                    containerMinLoadFactor = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case "containers_init_number" -> {
                    containersInitNumber.set(Integer.valueOf(value.intValue()));
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                default -> logger.log(Level.ERROR, "unsupported type - {}", key);
            }
        });
        port.setContainerMaxLoadFactor(containerMaxLoadFactor);
        port.setContainerMinLoadFactor(containerMinLoadFactor);
        port.setContainersNumber(containersInitNumber);
        port.setDocksNumber(docksNumber);
        port.setMaxContainersNumber(maxContainersNumber);
        port.initialise();
        return port;
    }
}
