package by.makei.seaport.entity;

import by.makei.seaport.exception.CustomException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class PortBuilderTest {
    public Port port;
    public PortBuilder builder;
    public Map<String,Double> testMap = new HashMap<>();

    @Test
    void getPort() throws CustomException {
        builder = new PortBuilder();
        testMap.put("max_containers", 555.0);
        testMap.put("docks_number", 7.0);
        testMap.put("container_max_load", 0.85);
        testMap.put("container_min_load", 0.15);
        testMap.put("containers_init_number", 250.0);

       port =  builder.getPort(testMap);
        System.out.println(port);

    }
}