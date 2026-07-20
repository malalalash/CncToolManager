package pl.cnc.manager.repository;

import pl.cnc.manager.model.Tool;

import java.util.List;

public interface ToolRepository {

    List<Tool> findAll();

    boolean existById(String id);

    void save(Tool tool);

    boolean deleteById(String id);

    boolean updateQuantity(String id, int quantity);

    boolean issueTool(String id, int amount);
}
