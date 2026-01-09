package gymmi.fixture;

import gymmi.workspace.service.domain.mission.Mission;
import gymmi.workspace.service.domain.workspace.Workspace;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class MissionFixture {

    public static MissionBuilder builder(Workspace workspace) {
        return new MissionBuilder(workspace);
    }

    public static class MissionBuilder {
        private final Workspace workspace;

        private Long id = null;
        private String name = "테스트 미션";
        private Integer score = Mission.MIN_SCORE;

        private MissionBuilder(Workspace workspace) {
            this.workspace = workspace;
        }

        public MissionBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MissionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MissionBuilder score(Integer score) {
            this.score = score;
            return this;
        }

        public Mission build() {
            Mission mission = Mission.builder()
                .workspace(workspace)
                .name(name)
                .score(score)
                .build();

            if (id != null) {
                ReflectionTestUtils.setField(mission, "id", id);
            }

            return mission;
        }
    }
}
