package app.rolly.backend.exception;

public class TrickAlreadyExistsException extends RuntimeException {
    public TrickAlreadyExistsException(String username) {
        super("Trick " + username + " Already Exists");
    }
}
