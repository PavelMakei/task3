package by.makei.seaport.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class WaterArea {
    private static final Logger logger = LogManager.getLogger();
    private final Semaphore semaphore = new Semaphore(10);
    private static WaterArea instance;

    private WaterArea(){}

    public static WaterArea getInstance() {
        if (instance == null) {
            synchronized (Port.class) {
                if (instance == null) {
                    instance = new WaterArea();
                }
            }
        }
        return instance;
    }

    public void getIntoWaterArea() throws InterruptedException {
        semaphore.acquire();
        logger.log(Level.INFO, "Ship {} entered to the water area", Thread.currentThread().getName());
    }

    public void getOutWaterArea () throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500);
        semaphore.release();
        logger.log(Level.INFO, "Ship {} left the water area", Thread.currentThread().getName());
    }


}
