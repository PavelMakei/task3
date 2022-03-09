package by.makei.seaport.parser.impl;

import by.makei.seaport.parser.PortStringParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PortStringParserImpl implements PortStringParser {
    private static final Logger logger = LogManager.getLogger();
    private static final AtomicReference<PortStringParserImpl> instance = new AtomicReference<>();
    private static final String REGEXP_STRING_SPLITTER = "\\s+";
    private static final String REGEXP_MAP_SPLITTER = ":";


    private PortStringParserImpl(){}

    public static PortStringParserImpl getInstance() {
        while (true) {
            PortStringParserImpl current = instance.get();
            if (current != null) {
                return current;
            }
            current = new PortStringParserImpl();
            if (instance.compareAndSet(null, current)) {
                return current;
            }
        }
    }


    @Override
    public Map<String, Double> parse(String portInit) {

        //TODO catch possible exceptions or validate?

        String[] strings = portInit.split(REGEXP_STRING_SPLITTER);
        Map<String, Double> result = new HashMap<>();
        for(int i = 0; i<strings.length; i++){
            String[] line = strings[i].strip().split(REGEXP_MAP_SPLITTER);
            result.put(line[0],Double.valueOf(line[1]));
        }
        return result;
    }
}
