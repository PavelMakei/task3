package by.makei.seaport.parser.impl;

import by.makei.seaport.parser.PortStringParser;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


class PortStringParserImplTest {
public PortStringParser parser = PortStringParserImpl.getInstance();
public String testString = """
        max_containers:500
        docks_number:5
        container_max_load:0.75
        container_min_load:0.25
        containers_init_number:300
        """;

    @Test
    void parseTest() {
        Map<String,Double> expected = new HashMap<>();
        expected.put("max_containers", 500.0);
        expected.put("docks_number", 5.0);
        expected.put("container_max_load", 0.75);
        expected.put("container_min_load", 0.25);
        expected.put("containers_init_number", 300.0);
        Map<String,Double> actual = parser.parse(testString);
        assert (expected.equals(actual));

    }
}