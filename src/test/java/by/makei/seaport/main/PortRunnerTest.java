package by.makei.seaport.main;

import by.makei.seaport.exception.CustomException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortRunnerTest {

    @Test
    void main() throws CustomException {
        while (true) {
            PortRunner.main(null);
        }
    }
}