package by.makei.seaport.main;

import by.makei.seaport.entity.Port;
import by.makei.seaport.entity.PortBuilder;
import by.makei.seaport.entity.Ship;
import by.makei.seaport.entity.ShipGenerator;
import by.makei.seaport.exception.CustomException;
import by.makei.seaport.parser.PortStringParser;
import by.makei.seaport.parser.impl.PortStringParserImpl;
import by.makei.seaport.reader.CustomFileReader;
import by.makei.seaport.reader.ZipFileReader;
import by.makei.seaport.reader.impl.CustomFileReaderImpl;
import by.makei.seaport.reader.impl.ZipFileReaderImpl;
import by.makei.seaport.service.PortLogistics;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PortRunnerZip {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATA_PORT_INIT_TXT = "port_init.txt";
    private static final String SHIPS_INIT_FILE_NAME = "ships_init.txt";
    private static final String ARCHIVE_FILE_NAME = "data/initialisation.zip";


    private Port port;


    public static void main(String[] args) throws CustomException {
        PortRunnerZip portRunner = new PortRunnerZip();
        portRunner.run();
    }

    private void run() throws CustomException {
        ShipGenerator shipGenerator = ShipGenerator.getInstance();
        List<Ship> ships;

        initialisePort();
        ships = shipGenerator.getShips(port, ARCHIVE_FILE_NAME,SHIPS_INIT_FILE_NAME);

        ExecutorService executor;
        executor = Executors.newFixedThreadPool(15);
        logger.log(Level.INFO, "\n\n <<<<<<<<<< START >>>>>>>>>>\n");

        ships.forEach(ship -> executor.execute(ship));
        executor.shutdown();
        startLogisticTimer();

        try {
            TimeUnit.MILLISECONDS.sleep(100);//waiting for any ship will be added to port
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "sleep was interrupted");
        }

        while (port.getShipCounter().intValue() > 0) {
            Thread.yield();
        }

        logger.log(Level.INFO, "\n\n <<<<<<<<<< PortRunner finished work >>>>>>>>>>\n");
        logger.log(Level.INFO, "\n{} - docks were gotten, {} - docks were returned\n", port.getDockGetCount(), port.getDockReturnCount());

        logger.log(Level.INFO, "\n\nAvailable containers in port value - {} \nContainers income value - {} \nContainers outcome value - {}",
                port.getContainersNumber().intValue(), port.getDebit().intValue(), port.getCredit().intValue());
    }

    private void startLogisticTimer() {
        Timer timer = new Timer(true);
        timer.schedule(PortLogistics.getInstance(port), 500, 1000);

    }

    private void initialisePort() throws CustomException {
        ZipFileReader reader = ZipFileReaderImpl.getInstance();
        String initDataText = reader.readLinesFromZipFile(ARCHIVE_FILE_NAME, DATA_PORT_INIT_TXT);
        PortStringParser parser = PortStringParserImpl.getInstance();
        Map initMap = parser.parse(initDataText);
        PortBuilder builder = new PortBuilder();
        port = builder.getPort(initMap);
    }
}
