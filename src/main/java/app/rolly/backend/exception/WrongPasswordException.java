package app.rolly.backend.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Wrong password!");
    }
}
