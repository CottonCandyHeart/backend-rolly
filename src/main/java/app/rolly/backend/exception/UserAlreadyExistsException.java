package app.rolly.backend.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Username " + username + " Already Exists");
    }

}
