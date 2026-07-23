package pl.cnc.manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.cnc.manager.model.OperationType;
import pl.cnc.manager.model.ToolIssue;
import pl.cnc.manager.model.ToolType;
import pl.cnc.manager.repository.ToolIssueRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolIssueServiceTest {

    @Mock
    private ToolIssueRepository repository;

    @InjectMocks
    private ToolIssueService service;

    private ToolIssue sampleIssue;

    @BeforeEach
    void setUp() {
        LocalDateTime issuedAt = LocalDateTime.of(2026, 7, 23, 14, 32);
        sampleIssue = new ToolIssue("1", "D1", "Drill 8mm", ToolType.DRILL, 3, OperationType.RETURN, issuedAt);
    }

    @Nested
    @DisplayName("Tests for findIssueHistory")
    class FindIssueHistory {

        @Test
        @DisplayName("Should return list of tool issues from the repository")
        void shouldReturnListOfIssues() {
            List<ToolIssue> expectedIssues = List.of(sampleIssue);
            when(repository.findIssueHistory()).thenReturn(expectedIssues);

            List<ToolIssue> actualList = service.getIssueHistory();

            assertEquals(1, actualList.size());
            assertEquals(sampleIssue, actualList.getFirst());
            verify(repository, times(1)).findIssueHistory();
        }

        @Test
        @DisplayName("Should return empty list when repository is empty")
        void shouldReturnEmptyListWhenNoIssuesExist() {
            when(repository.findIssueHistory()).thenReturn(Collections.emptyList());

            List<ToolIssue> actualIssues = service.getIssueHistory();

            assertTrue(actualIssues.isEmpty());
            verify(repository, times(1)).findIssueHistory();
        }
    }
}