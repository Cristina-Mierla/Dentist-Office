package exceptions;

public class RepositoryException extends RuntimeException{
    public RepositoryException(){}

    public RepositoryException(String message){
        super(message);
    }

    public RepositoryException(Exception except){
        super(except);
    }
}
