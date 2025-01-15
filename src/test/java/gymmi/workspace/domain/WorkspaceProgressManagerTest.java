package gymmi.workspace.domain;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.*;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkspaceProgressManagerTest {

    @Test
    void 워크스페이스가_진행중이_아닌_경우_예외가_발생한다() {
        // given
        WorkspaceStatus workspaceStatusExcluding = getWorkspaceStatusExcluding(WorkspaceStatus.IN_PROGRESS);
        Workspace workspace = getWorkspace(workspaceStatusExcluding, Workspace.MIN_HEAD_COUNT);
        List<Mission> missions = getMissions(workspace, 3);

        // when, then
        assertThatThrownBy(() -> new WorkspaceProgressManager(workspace, missions, 0))
                .hasMessage(ErrorCode.INACTIVE_WORKSPACE.getMessage());
    }

    @Test
    void 페이즈_변화_여부를_확인한다() {
        // given
        Workspace workspace = getWorkspace(WorkspaceStatus.IN_PROGRESS, Workspace.MIN_HEAD_COUNT, 100);
        List<Mission> missions = getMissions(workspace, 3);
        WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions, 10);
        assertThat(workspaceProgressManager.getWorkspacePhase()).isEqualTo(WorkspacePhase.P_0);

        // when
        boolean result = workspaceProgressManager.hasPhaseChanged(25);

        // then
        assertThat(result).isEqualTo(true);
        assertThat(workspaceProgressManager.getWorkspacePhase()).isEqualTo(WorkspacePhase.P_25);
    }


    @Nested
    class 운동_하기 {

        @Test
        void 워크스페이스_참여자가_아닌_경우_예외가_발생한다() {
            // given
            Workspace workspace = getWorkspace(WorkspaceStatus.IN_PROGRESS, Workspace.MIN_HEAD_COUNT);
            List<Mission> missions = getMissions(workspace, 3);
            WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions, 0);

            Worker worker = Instancio.of(Worker.class)
                    .filter(Select.field(Worker::getWorkspace), (Workspace ws) -> !ws.equals(workspace))
                    .create();
            Map<Mission, Integer> workouts = Map.of(missions.get(0), 1);
            WorkoutConfirmation workoutProof = Instancio.of(WorkoutConfirmation.class).create();

            // when, then
            assertThatThrownBy(
                    () -> workspaceProgressManager.doWorkout(worker, workouts, workoutProof)
            ).hasMessage(ErrorCode.NOT_JOINED_WORKSPACE.getMessage());

        }

        @Test
        void 워크스페이스에_동록된_미션이_아닌_경우_예외가_발생한다() {
            Workspace workspace = getWorkspace(WorkspaceStatus.IN_PROGRESS, Workspace.MIN_HEAD_COUNT);
            List<Mission> missions = getMissions(workspace, 3);
            WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions, 0);

            Worker worker = getWorker(workspace);
            Mission mission = Instancio.of(Mission.class)
                    .filter(Select.field(Mission::getWorkspace), (Workspace ws) -> !ws.equals(workspace))
                    .create();
            Map<Mission, Integer> workouts = Map.of(mission, 1);
            WorkoutConfirmation workoutProof = Instancio.of(WorkoutConfirmation.class).create();

            // when, then
            assertThatThrownBy(() -> workspaceProgressManager.doWorkout(worker, workouts, workoutProof))
                    .hasMessage(ErrorCode.NOT_REGISTERED_WORKSPACE_MISSION.getMessage());
        }

        @Test
        void 운동을_한다() {
            // given
            Workspace workspace = getWorkspace(WorkspaceStatus.IN_PROGRESS, Workspace.MIN_HEAD_COUNT);
            List<Mission> missions = getMissions(workspace, 3);
            WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions, 0);

            Worker worker = getWorker(workspace);
            Mission mission = missions.get(0);
            Mission mission1 = missions.get(1);
            Integer missionCount = 1;
            Integer missionCount1 = 2;
            Map<Mission, Integer> workouts = Map.of(mission, missionCount, mission1, missionCount1);

            int sum = mission.getScore() * missionCount + mission1.getScore() * missionCount1;
            WorkoutConfirmation workoutProof = Instancio.of(WorkoutConfirmation.class).create();

            // when
            WorkoutHistory workoutHistory = workspaceProgressManager.doWorkout(worker, workouts, workoutProof);

            // then
            assertThat(workoutHistory.getSum()).isEqualTo(sum);
            assertThat(workoutHistory.getWorkoutRecords()).hasSize(2);
//            assertThat(worked.getWorkoutRecords()).containsExactlyInAnyOrder(
//                            new WorkoutRecord(mission, missionCount),
//                            new WorkoutRecord(mission1, missionCount1)
//                    );
        }

        @Test
        void 워크스페이스의_목표점수에_도달하는_경우_워크스페이스는_완료된다() {
            // given
            Workspace workspace = getWorkspace(WorkspaceStatus.IN_PROGRESS, Workspace.MIN_HEAD_COUNT);
            List<Mission> missions = getMissions(workspace, 1);
            WorkspaceProgressManager workspaceProgressManager = new WorkspaceProgressManager(workspace, missions, 0);

            // when
            workspaceProgressManager.completeWhenGoalScoreIsAchieved(workspace.getGoalScore() + 1);

            // then
            assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.COMPLETED);
        }
    }

    private WorkspaceStatus getWorkspaceStatusExcluding(WorkspaceStatus... workspaceStatus) {
        return Instancio.gen()
                .enumOf(WorkspaceStatus.class)
                .excluding(workspaceStatus)
                .get();
    }

    private List<Mission> getMissions(Workspace workspace, int size) {
        List<Mission> missions = Instancio.ofList(Mission.class)
                .size(size)
                .set(Select.field(Mission::getWorkspace), workspace)
                .generate(Select.field(Mission::getScore),
                        gen -> gen.ints().range(Mission.MIN_SCORE, Mission.MAX_SCORE))
                .withUnique(Select.field(Mission::getId))
                .create();
        return missions;
    }

    private Worker getWorker(Workspace workspace, Long... excludingWorkerIds) {
        List<Long> excludingIds = Arrays.stream(excludingWorkerIds).toList();
        return Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace)
                .filter(Select.field(Worker::getId), (Long id) -> !excludingIds.contains(id))
                .create();
    }

    private Workspace getWorkspace(WorkspaceStatus workspaceStatus, int headCount) {
        Workspace workspace = Instancio.of(Workspace.class)
                .set(Select.field(Workspace::getStatus), workspaceStatus)
                .set(Select.field(Workspace::getHeadCount), headCount)
                .create();
        return workspace;
    }

    private Workspace getWorkspace(WorkspaceStatus workspaceStatus, int headCount, int goalScore) {
        Workspace workspace = Instancio.of(Workspace.class)
                .set(Select.field(Workspace::getStatus), workspaceStatus)
                .set(Select.field(Workspace::getHeadCount), headCount)
                .set(Select.field(Workspace::getGoalScore), goalScore)
                .create();
        return workspace;
    }
}
