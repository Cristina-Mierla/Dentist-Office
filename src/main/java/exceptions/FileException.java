package exceptions;

import java.io.IOException;

public class FileException extends IOException {
    public FileException(){}

    public FileException(String message){
        super("File exception found: " + message);
    }

    public FileException(Exception except){
        super(except);
    }
}
