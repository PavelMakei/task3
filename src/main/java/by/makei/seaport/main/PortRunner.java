package by.makei.seaport.main;

import by.makei.seaport.entity.Port;
import by.makei.seaport.entity.PortBuilder;
import by.makei.seaport.entity.Ship;
import by.makei.seaport.exception.CustomException;
import by.makei.seaport.parser.PortStringParser;
import by.makei.seaport.parser.impl.PortStringParserImpl;
import by.makei.seaport.reader.CustomFileReader;
import by.makei.seaport.reader.impl.CustomFileReaderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PortRunner {
    private String fileName = "data/init.txt";
    private Port port;

    public static void main(String[] args) throws CustomException {
        PortRunner portRunner = new PortRunner();
        portRunner.run();
    }

    private void run() throws CustomException {
        initialisePort();
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship("ship1",port,100, 100));
        ships.add(new Ship("ship2",port,100, 100));
        ships.add(new Ship("ship3",port,100, 0));
        ships.add(new Ship("ship4",port,100, 0));
        ships.add(new Ship("ship5",port,100, 100));
        ships.add(new Ship("ship6",port,100, 0));
        ships.add(new Ship("ship7",port,100, 100));
        ships.add(new Ship("ship8",port,100, 0));

        ships.forEach(ship -> {
            ship.start();
            try {
                ship.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(port);



    }

    private void initialisePort() throws CustomException {
       CustomFileReader reader = CustomFileReaderImpl.getInstance();
        String initDataText = reader.readLinesFromFile(fileName);
        PortStringParser parser = PortStringParserImpl.getInstance();
        Map initMap = parser.parse(initDataText);
        PortBuilder builder = new PortBuilder();
        port = builder.getPort(initMap);
        System.out.println(port);



    }
}
