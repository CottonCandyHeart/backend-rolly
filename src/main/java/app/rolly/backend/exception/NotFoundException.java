package app.rolly.backend.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s + " Not Found");
    }
}
