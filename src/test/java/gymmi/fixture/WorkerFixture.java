package gymmi.fixture;

import gymmi.user.domain.User;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class WorkerFixture {

    public static WorkerBuilder builder(User user, Workspace workspace) {
        return new WorkerBuilder(user, workspace);
    }

    public static class WorkerBuilder {
        private final User user;
        private final Workspace workspace;

        private Long id = 0L;
        private Integer contributedScore = 0;

        private WorkerBuilder(User user, Workspace workspace) {
            this.user = user;
            this.workspace = workspace;
        }

        public WorkerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WorkerBuilder contributedScore(Integer contributedScore) {
            this.contributedScore = contributedScore;
            return this;
        }

        // 2. 최종 객체 생성 및 리플렉션 주입
        public Worker build() {
            Worker worker = new Worker(user, workspace);
            if (id != null) {
                ReflectionTestUtils.setField(worker, "id", id);
            }
            ReflectionTestUtils.setField(worker, "contributedScore", contributedScore);
            return worker;
        }
    }
}
