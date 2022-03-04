package by.makei.seaport.reader;

import by.makei.seaport.exception.CustomException;

public interface CustomFileReader {
    String readLinesFromFile(String stringFileName) throws CustomException, CustomException;
}
