package by.makei.seaport.reader.impl;


import by.makei.seaport.entity.Port;
import by.makei.seaport.exception.CustomException;
import by.makei.seaport.reader.CustomFileReader;
import by.makei.seaport.util.CustomFileUtil;
import by.makei.seaport.util.impl.CustomFileUtilImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;


public class CustomFileReaderImpl implements CustomFileReader {

    private static final Logger logger = LogManager.getLogger();
    private static final AtomicReference<CustomFileReaderImpl> instance = new AtomicReference<>();

    private CustomFileReaderImpl() {}

    public static CustomFileReaderImpl getInstance() {
        while (true) {
            CustomFileReaderImpl current = instance.get();
            if (current != null) {
                return current;
            }
            current = new CustomFileReaderImpl();
            if (instance.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    @Override
    public String readLinesFromFile(String stringFileName) throws CustomException {
        CustomFileUtil fileUtil = CustomFileUtilImpl.getInstance();
        File file = fileUtil.getFileFromStringForResourcesPackage(stringFileName);
        Path path = file.toPath();
        try {
        return Files.readString(path);
        } catch (IOException e) {
            logger.log(Level.ERROR, "file wasn't read", e);
            throw new CustomException("file wasn't read", e);
        }
    }
}
