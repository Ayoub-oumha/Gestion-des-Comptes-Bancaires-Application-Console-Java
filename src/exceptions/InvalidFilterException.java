package exceptions;

public class InvalidFilterException extends IllegalArgumentException {
    public InvalidFilterException(String message) {
        super(message);
    }
}
