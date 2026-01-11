package gymmi.workspace.domain;

import gymmi.etc.domain.entity.User;
import gymmi.fixture.*;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.mission.domain.entity.Mission;
import gymmi.workspace.workout.controller.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.workout.controller.request.WorkoutRequest;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workout.domain.entity.WorkoutConfirmation;
import gymmi.workspace.workout.domain.entity.WorkoutHistory;
import gymmi.workspace.workout.domain.entity.WorkoutRecord;
import gymmi.workspace.workout.domain.WorkoutValidator;
import gymmi.workspace.workspace.domain.entity.Workspace;
import gymmi.workspace.workspace.domain.WorkspacePhase;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.workout.domain.WorkoutProcessor;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class WorkoutProcessorTest {

    @Test
    void 운동을_하면_워크스페이스_페이즈가_갱신되며_점수가_반영된다_또한_워크스페이스_목표_점수_달성시_워크스페이스는_종료_된다() {
        // given
        User user = UserFixture.defaultUser();
        Workspace workspace = WorkspaceFixture.builder(user)
                .workspaceStatus(WorkspaceStatus.IN_PROGRESS)
                .goalScore(100)
                .currentScore(90)
                .build();
        Worker worker = WorkerFixture.builder(user, workspace).contributedScore(10).build();

        Mission mission = MissionFixture.builder(workspace).score(5).build();
        WorkoutConfirmation workoutConfirmation = WorkoutConfirmationFixture.builder().build();
        WorkoutHistory workoutHistory = WorkoutHistoryFixture.builder(worker, workoutConfirmation).totalScore(0).build();
        WorkoutRecord workoutRecord = WorkoutRecordFixture.builder(workoutHistory, mission).count(2).build();

        WorkoutProcessor workoutProcessor = new WorkoutProcessor(workspace, worker, List.of(workoutRecord));

        // when
        workoutProcessor.apply();

        // then
        assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.COMPLETED);
        assertThat(workoutProcessor.isPhaseChanged()).isTrue();
        assertThat(workoutProcessor.getWorkspacePhase()).isEqualTo(WorkspacePhase.P_100);
        assertThat(workoutRecord.getSum()).isEqualTo(10);
        assertThat(workoutHistory.getTotalScore()).isEqualTo(10);
        assertThat(worker.getContributedScore()).isEqualTo(20);
        assertThat(workspace.getCurrentScore()).isEqualTo(100);
    }

}
