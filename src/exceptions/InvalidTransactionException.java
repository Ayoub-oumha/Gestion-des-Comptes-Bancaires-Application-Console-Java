package exceptions;

public class InvalidTransactionException extends IllegalStateException{
    public InvalidTransactionException(String message) {
        super(message);
    }
}
