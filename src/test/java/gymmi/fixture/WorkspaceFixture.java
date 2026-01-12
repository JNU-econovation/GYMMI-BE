package gymmi.fixture;

import gymmi.etc.domain.entity.User;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.domain.WorkspaceCreationValidator;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class WorkspaceFixture {

    public static WorkspaceBuilder builder(User creator) {
        return new WorkspaceBuilder(creator);
    }

    public static class WorkspaceBuilder {
        private final User creator;

        private Long id = 0L;
        private String name = "방이름";
        private String description = "워크스페이스 설명";
        private Integer goalScore = WorkspaceCreationValidator.MIN_GOAL_SCORE;
        private Integer headCount = WorkspaceCreationValidator.MAX_HEAD_COUNT;
        private String tag = "운동";
        private String task = "치킨 내기";
        private WorkspaceStatus workspaceStatus = WorkspaceStatus.PREPARING;
        private Integer currentScore = 0;
        private String password = "1234";

        private WorkspaceBuilder(User creator) {
            this.creator = creator;
        }

        public WorkspaceBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WorkspaceBuilder name(String name) {
            this.name = name;
            return this;
        }

        public WorkspaceBuilder password(String password) {
            this.password = password;
            return this;
        }

        public WorkspaceBuilder description(String description) {
            this.description = description;
            return this;
        }

        public WorkspaceBuilder goalScore(Integer goalScore) {
            this.goalScore = goalScore;
            return this;
        }

        public WorkspaceBuilder headCount(Integer headCount) {
            this.headCount = headCount;
            return this;
        }

        public WorkspaceBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public WorkspaceBuilder task(String task) {
            this.task = task;
            return this;
        }

        public WorkspaceBuilder workspaceStatus(WorkspaceStatus workspaceStatus) {
            this.workspaceStatus = workspaceStatus;
            return this;
        }

        public WorkspaceBuilder currentScore(Integer currentScore) {
            this.currentScore = currentScore;
            return this;
        }

        public Workspace build() {
            Workspace workspace = new Workspace(
                    creator,
                    name,
                    description,
                    goalScore,
                    headCount,
                    tag,
                    task
            );

            if (id != null) {
                ReflectionTestUtils.setField(workspace, "id", id);
            }
            ReflectionTestUtils.setField(workspace, "status", workspaceStatus);
            ReflectionTestUtils.setField(workspace, "currentScore", currentScore);
            ReflectionTestUtils.setField(workspace, "password", password);

            return workspace;
        }
    }
}
