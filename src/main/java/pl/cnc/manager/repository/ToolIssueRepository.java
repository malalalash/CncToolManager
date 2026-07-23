package pl.cnc.manager.repository;

import pl.cnc.manager.model.Tool;
import pl.cnc.manager.model.ToolIssue;

import java.util.List;

public interface ToolIssueRepository {

    List<ToolIssue> findIssueHistory();

}
