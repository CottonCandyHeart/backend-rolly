package app.rolly.backend.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String c) {
        super("Category " + c + " Already Exists");
    }
}
