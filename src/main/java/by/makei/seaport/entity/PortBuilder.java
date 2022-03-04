package by.makei.seaport.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PortBuilder {
    private static final Logger logger = LogManager.getLogger();
    private static final String MAX_CONTAINERS_ON_SHIP = "max_containers";
    private static final String DOCKS_NUMBER = "docks_number";
    private static final String CONTAINER_MAX_LOAD_FACTOR = "container_max_load";
    private static final String CONTAINER_MIN_LOAD_FACTOR = "container_min_load";
    private static final String CONTAINERS_INIT_NUMBER = "containers_init_number";

    //set defaults
    private double maxContainersNumber = 300;
    private double docksNumber = 5;
    private double containerMaxLoadFactor = 0.75;
    private double containerMinLoadFactor = 0.25;
    private final AtomicInteger containersInitNumber = new AtomicInteger(300);

    public Port getPort(Map<String, Double> initMap)  {
        Port port = Port.getInstance();
        initMap.forEach((key, value) -> {
            switch (key) {
                case MAX_CONTAINERS_ON_SHIP -> {
                    maxContainersNumber = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case DOCKS_NUMBER -> {
                    docksNumber = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case CONTAINER_MAX_LOAD_FACTOR -> {
                    containerMaxLoadFactor = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case CONTAINER_MIN_LOAD_FACTOR -> {
                    containerMinLoadFactor = value;
                    logger.log(Level.INFO, "{} is set = {}",key, value);
                }
                case CONTAINERS_INIT_NUMBER -> {
                    containersInitNumber.set(value.intValue());
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
