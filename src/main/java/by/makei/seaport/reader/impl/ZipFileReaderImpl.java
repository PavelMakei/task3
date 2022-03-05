package by.makei.seaport.reader.impl;


import by.makei.seaport.exception.CustomException;
import by.makei.seaport.util.CustomFileUtil;
import by.makei.seaport.reader.ZipFileReader;
import by.makei.seaport.util.impl.CustomFileUtilImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFileReaderImpl implements ZipFileReader {
    private static final int BUFFER_SIZE = 4096;
    private static final Logger logger = LogManager.getLogger();
    private static final ZipFileReaderImpl instance = new ZipFileReaderImpl();
    private static final String WINDOWS_FILE_SEPARATOR = "\\";
    private static final String URL_FILE_SEPARATOR = "/";

    private ZipFileReaderImpl() {}

    public static ZipFileReaderImpl getInstance() {
        return instance;
    }


    @Override
    public String readLinesFromZipFile(String zipFileName, String stringFileName) throws CustomException {
        CustomFileUtil fileUtil = CustomFileUtilImpl.getInstance();
        File file = fileUtil.getFileFromStringForResourcesPackage(zipFileName);
        Path zipFilePath = file.toPath();

        try (
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath.toString()));
        ) {
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                if (!entry.isDirectory()) {
                    // if the entry is a file, and has equals name extracts it
                    if (entry.getName().equals(stringFileName)) {
                        return new String(zipIn.readAllBytes(), StandardCharsets.UTF_8);
                    }
                }
                entry = zipIn.getNextEntry();
            }
        }catch (IOException e){
            logger.log(Level.ERROR,"Archive {} didn't found or can't bre read", zipFilePath.toString());
            throw new CustomException("Archive " + zipFilePath.toString() + " did not found or can't bre read");
        }
        logger.log(Level.ERROR,"File {} didn't found in archive",  stringFileName);
        throw new CustomException("File " +  stringFileName + " didn't found in archive");
    }
}
