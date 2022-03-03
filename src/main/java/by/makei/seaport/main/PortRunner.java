package by.makei.seaport.main;

import by.makei.seaport.entity.Port;
import by.makei.seaport.entity.PortBuilder;
import by.makei.seaport.entity.Ship;
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
    private String fileName = "data/init.txt";
    private Port port;


    public static void main(String[] args) throws CustomException {
        PortRunner portRunner = new PortRunner();
        portRunner.run();
    }

    private void run() throws CustomException {
        initialisePort();
        List<Ship> ships = ShipGenerator.getShips(20, 70, port);

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
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "something go wrong", e);
            }
        }
        logger.log(Level.INFO, "PortRunner finished work");
        logger.log(Level.INFO, "\nAvailable containers value - {} \nContainers debit - {} \nContainers credit - {}",
                port.getContainersNumber().intValue(), port.getDebit().intValue(), port.getCredit().intValue());

    }

    private void initialisePort() throws CustomException {
        CustomFileReader reader = CustomFileReaderImpl.getInstance();
        String initDataText = reader.readLinesFromFile(fileName);
        PortStringParser parser = PortStringParserImpl.getInstance();
        Map initMap = parser.parse(initDataText);
        PortBuilder builder = new PortBuilder();
        port = builder.getPort(initMap);


    }
}
