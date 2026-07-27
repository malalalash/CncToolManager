package pl.cnc.manager.repository;

public class ToolInUseException extends ToolRepositoryException {
    public ToolInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
