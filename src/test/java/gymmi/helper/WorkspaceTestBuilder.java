package gymmi.helper;

import gymmi.entity.User;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.domain.WorkspaceStatus;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class WorkspaceTestBuilder {

    private Long id;
    private User creator;
    private String name;
    private String password;
    private String description;
    private WorkspaceStatus status;
    private Integer goalScore;
    private Integer headCount;
    private LocalDateTime createdAt;
    private String tag;

    public WorkspaceTestBuilder() {
    }

    public Workspace build() {
        Workspace workspace = Workspace.builder()
                .creator(creator)
                .name(name)
                .description(description)
                .goalScore(goalScore)
                .headCount(headCount)
                .tag(tag)
                .build();
        ReflectionTestUtils.setField(workspace, "id", id);
        ReflectionTestUtils.setField(workspace, "password", password);
        ReflectionTestUtils.setField(workspace, "createdAt", createdAt);
        ReflectionTestUtils.setField(workspace, "status", status);
        return workspace;
    }

    public WorkspaceTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public WorkspaceTestBuilder creator(User creator) {
        this.creator = creator;
        return this;
    }

    public WorkspaceTestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public WorkspaceTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public WorkspaceTestBuilder description(String description) {
        this.description = description;
        return this;
    }

    public WorkspaceTestBuilder withStatus(WorkspaceStatus status) {
        this.status = status;
        return this;
    }

    public WorkspaceTestBuilder goalScore(Integer goalScore) {
        this.goalScore = goalScore;
        return this;
    }

    public WorkspaceTestBuilder headCount(Integer headCount) {
        this.headCount = headCount;
        return this;
    }

    public WorkspaceTestBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public WorkspaceTestBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

}
