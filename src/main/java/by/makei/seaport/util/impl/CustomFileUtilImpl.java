package by.makei.seaport.util.impl;

import by.makei.seaport.util.CustomFileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;

public class CustomFileUtilImpl implements CustomFileUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final CustomFileUtilImpl instance = new CustomFileUtilImpl();
    private static final String WINDOWS_FILE_SEPARATOR = "\\";
    private static final String URL_FILE_SEPARATOR = "/";

    private CustomFileUtilImpl(){}

    public static CustomFileUtilImpl getInstance(){
        return instance;
    }

    /**
     * Makes full File reference through reflection and getting URL.
     * Allows get files from src/main/resourced folder
     *
     * @param fileName  String
     * @return file File
     */


    @Override
    public File getFileFromStringForResourcesPackage(@NotNull String fileName) {

        fileName = fileName.replace(WINDOWS_FILE_SEPARATOR, URL_FILE_SEPARATOR);
        File file;
        URL url = getClass().getClassLoader().getResource(fileName);
        file = new File(url.getFile());
        return file;
    }
}
