package by.makei.seaport.main;

import by.makei.seaport.entity.Port;
import by.makei.seaport.entity.PortBuilder;
import by.makei.seaport.entity.Ship;
import by.makei.seaport.entity.ShipGenerator;
import by.makei.seaport.exception.CustomException;
import by.makei.seaport.parser.PortStringParser;
import by.makei.seaport.parser.impl.PortStringParserImpl;
import by.makei.seaport.reader.CustomFileReader;
import by.makei.seaport.reader.impl.CustomFileReaderImpl;
import by.makei.seaport.service.PortLogistics;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PortRunner {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATA_PORT_INIT_TXT = "data/port_init.txt";
    private static final String SHIPS_INIT_FILE_NAME = "data/ships_init.txt";

    private Port port;


    public static void main(String[] args) throws CustomException {
        PortRunner portRunner = new PortRunner();
        portRunner.run();
    }

    private void run() throws CustomException {
        ShipGenerator shipGenerator = ShipGenerator.getInstance();
        List<Ship> ships;

        initialisePort();
        ships = shipGenerator.getShips(port, SHIPS_INIT_FILE_NAME);
        logger.log(Level.INFO, "\n <<<<<<<<<< START >>>>>>>>>>\n");
        ships.forEach(ship -> ship.start());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "sleep was interrupted");
        }

        while (port.getShipCounter().intValue() > 0) {
            PortLogistics portLogistics = new PortLogistics(port);
            portLogistics.start();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "something go wrong", e);
            }
        }
        logger.log(Level.INFO, "PortRunner finished work");
        logger.log(Level.INFO, "\nAvailable containers in port value - {} \nContainers income value - {} \nContainers outcome value - {}",
                port.getContainersNumber().intValue(), port.getDebit().intValue(), port.getCredit().intValue());
    }

    private void initialisePort() throws CustomException {
        CustomFileReader reader = CustomFileReaderImpl.getInstance();
        String initDataText = reader.readLinesFromFile(DATA_PORT_INIT_TXT);
        PortStringParser parser = PortStringParserImpl.getInstance();
        Map initMap = parser.parse(initDataText);
        PortBuilder builder = new PortBuilder();
        port = builder.getPort(initMap);
    }
}
