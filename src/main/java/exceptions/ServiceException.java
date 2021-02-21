package exceptions;

public class ServiceException extends RuntimeException{
    public ServiceException(){}

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(Exception except){
        super(except);
    }
}