package by.makei.seaport.main;

import by.makei.seaport.entity.Port;
import by.makei.seaport.entity.Ship;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShipGenerator {
    private static final Logger logger = LogManager.getLogger();

    private ShipGenerator() {
    }

    //TODO correct factory

    public static List<Ship> getShips(int shipsNumber, int loadFactor, Port port) {
        Random random = new Random();
        Ship ship;
        List<Ship> ships = new ArrayList<>();

        for (int i = 0; i < shipsNumber; i++) {
            int containersMaxNumber = random.nextInt(20, 100);
            boolean containersPresent = (random.nextInt(100) > 70);
            int containersNumber = containersPresent ? random.nextInt(containersMaxNumber / 2, containersMaxNumber) : 0;
            ship = new Ship("ship " + i, port, containersMaxNumber, containersNumber);
            ships.add(ship);
        }
        logger.log(Level.INFO, "{} ships created", ships.size());
        return ships;
    }


}
