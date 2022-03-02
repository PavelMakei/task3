package by.makei.seaport.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface CustomFileUtil {

    /**
     * Makes full File reference through reflection and getting URL.
     * Allows get files from src/main/resourced folder
     *
     * @param fileName  String
     * @return file File
     */
    File getFileFromStringForResourcesPackage(@NotNull String fileName);
}
