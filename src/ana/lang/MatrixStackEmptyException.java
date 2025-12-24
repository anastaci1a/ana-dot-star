package ana.lang;

public class MatrixStackEmptyException extends RuntimeException {
    public MatrixStackEmptyException(String message) {
        super(message);
    }

    public MatrixStackEmptyException() {
        this("No remaining matrices to pop.");
    }
}
