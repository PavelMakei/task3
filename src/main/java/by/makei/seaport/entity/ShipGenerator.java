package by.makei.seaport.entity;

import by.makei.seaport.exception.CustomException;
import by.makei.seaport.parser.PortStringParser;
import by.makei.seaport.parser.impl.PortStringParserImpl;
import by.makei.seaport.reader.CustomFileReader;
import by.makei.seaport.reader.ZipFileReader;
import by.makei.seaport.reader.impl.CustomFileReaderImpl;
import by.makei.seaport.reader.impl.ZipFileReaderImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShipGenerator {
    private static final Logger logger = LogManager.getLogger();
    private static volatile ShipGenerator instance;
    private static final String SHIP_MAX_CONTAINERS = "ship_max_containers";
    private static final String SHIP_IS_EMPTY_PROBABILITY = "ship_is_empty_probability";
    private static final String SHIPS_NUMBER = "ships_number";

    private double maxContainersOnShip;
    private double shipIsEmptyProbability; // min 0, max 1.0
    private double shipsNumber;


    private ShipGenerator() {
    }

    public static ShipGenerator getInstance() { //double-checked locking
        if (instance == null) {
            synchronized (Port.class) {
                if (instance == null) {
                    instance = new ShipGenerator();
                }
            }
        }
        return instance;
    }

    //TODO correct factory?

    public List<Ship> getShips(Port port, String shipsInitFileName) throws CustomException {
        setValues(readValuesFromFile(shipsInitFileName));
        List<Ship> ships = buildShipsList(port);
        return ships;
    }

    public List<Ship> getShips(Port port, String archiveFileName, String shipsInitFileName) throws CustomException {
        setValues(readValuesFromZipFile(archiveFileName, shipsInitFileName));
        List<Ship> ships = buildShipsList(port);
        return ships;
    }

    @NotNull
    private List<Ship> buildShipsList(Port port) {
        Random random = new Random();
        Ship ship;
        List<Ship> ships = new ArrayList<>();

        for (int i = 0; i < shipsNumber; i++) {

            boolean containersPresent = random.nextFloat() < shipIsEmptyProbability;
            int containersNumber = containersPresent ? random.nextInt((int) maxContainersOnShip / 2, (int) maxContainersOnShip) : 0;
            ship = new Ship("ship " + i, port, (int) maxContainersOnShip, containersNumber);
            ships.add(ship);
        }
        logger.log(Level.INFO, "{} ships created", ships.size());
        return ships;
    }

    private String readValuesFromFile(String shipsInitFileName) throws CustomException {
        CustomFileReader reader = CustomFileReaderImpl.getInstance();
        return reader.readLinesFromFile(shipsInitFileName);
    }

    private String readValuesFromZipFile(String archiveFileName, String shipsInitFileName) throws CustomException {
        ZipFileReader reader = ZipFileReaderImpl.getInstance();
        return reader.readLinesFromZipFile(archiveFileName, shipsInitFileName);
    }

    private void setValues(String initDataText) {
        PortStringParser parser = PortStringParserImpl.getInstance();
        Map<String, Double> initMap = parser.parse(initDataText);

        initMap.forEach((key, value) -> {
            switch (key) {
                case SHIP_MAX_CONTAINERS -> {
                    maxContainersOnShip = value;
                    logger.log(Level.INFO, "{} is set = {}", key, value);
                }
                case SHIP_IS_EMPTY_PROBABILITY -> {
                    shipIsEmptyProbability = value;
                    logger.log(Level.INFO, "{} is set = {}", key, value);
                }
                case SHIPS_NUMBER -> {
                    shipsNumber = value;
                    logger.log(Level.INFO, "{} is set = {}", key, value);
                }
                default -> logger.log(Level.ERROR, "unsupported type - {}", key);
            }
        });
    }

}
