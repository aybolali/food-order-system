package domain.exception;

public class DomainExceptionClass extends RuntimeException{
    public DomainExceptionClass(String message) {
        super(message);
    }

    public DomainExceptionClass(String message, Throwable cause) {
        super(message, cause);
    }
}
