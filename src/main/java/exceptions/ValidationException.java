package exceptions;

public class ValidationException extends RuntimeException{
    public ValidationException(){}

    public ValidationException(String message){super("Invalid input: " + message);}

    public ValidationException(Exception except){super(except);}
}
