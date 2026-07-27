package pl.cnc.manager.repository;

public class DuplicateToolException extends ToolRepositoryException {
    public DuplicateToolException(String message, Throwable cause) {
        super(message, cause);
    }
}
