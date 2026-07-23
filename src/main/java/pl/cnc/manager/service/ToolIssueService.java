package pl.cnc.manager.service;

import pl.cnc.manager.model.ToolIssue;
import pl.cnc.manager.repository.ToolIssueRepository;

import java.util.List;


public class ToolIssueService {

    private final ToolIssueRepository repository;

    public ToolIssueService (ToolIssueRepository repository) {
        this.repository = repository;
    }

    public List<ToolIssue> getIssueHistory() {
        return repository.findIssueHistory();
    }
}
