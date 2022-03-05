package by.makei.seaport.reader;


import by.makei.seaport.exception.CustomException;

public interface ZipFileReader {
    String readLinesFromZipFile(String zipFileName, String stringFileName) throws CustomException;
}
