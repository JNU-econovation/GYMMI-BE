package gymmi.workspace.domain.entity;

import gymmi.entity.User;
import gymmi.fixture.*;
import gymmi.workspace.service.domain.workspace.WorkspaceStatus;
import gymmi.workspace.service.domain.workspace.Worker;
import gymmi.workspace.service.domain.workout.WorkoutConfirmation;
import gymmi.workspace.service.domain.workout.WorkoutHistory;
import gymmi.workspace.service.domain.workout.WorkoutValidator;
import gymmi.workspace.service.domain.workspace.Workspace;
import gymmi.workspace.repository.WorkoutHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static gymmi.exceptionhandler.message.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutValidatorTest {

    @Mock
    WorkoutHistoryRepository workoutHistoryRepository;

    @InjectMocks
    WorkoutValidator workoutValidator;

    @Test
    void 워크스페이스가_진행중이_아닌_경우_예외가_발생한다() {
        // given
        User user = UserFixture.defaultUser();
        Workspace workspace = WorkspaceFixture.builder(user)
                .id(1L)
                .workspaceStatus(WorkspaceStatus.PREPARING)
                .build();
        Worker worker = WorkerFixture.builder(user, workspace).build();

        // when, then
        assertThatThrownBy(() -> workoutValidator.validateCanWork(workspace, worker))
                .hasMessage(INACTIVE_WORKSPACE.getMessage());
    }

    @Test
    void 워크스페이스_참여자가_아닌_경우_예외가_발생한다() {
        // given
        User user = UserFixture.defaultUser();
        Workspace workspace = WorkspaceFixture.builder(user)
                .id(1L)
                .workspaceStatus(WorkspaceStatus.PREPARING)
                .build();

        Worker worker = WorkerFixture.defaultWorker();

        // when, then
        assertThatThrownBy(() -> workoutValidator.validateCanWork(workspace, worker))
                .hasMessage(NOT_JOINED_WORKSPACE.getMessage());

    }

    @Test
    void 워크스페이스_일일_기록이_횟수가_초과인_경우_예외가_발생한다() {
        // given
        User user = UserFixture.defaultUser();
        Workspace workspace = WorkspaceFixture.builder(user)
                .id(1L)
                .workspaceStatus(WorkspaceStatus.IN_PROGRESS)
                .build();

        Worker worker = WorkerFixture.builder(user, workspace).build();

        WorkoutConfirmation workoutConfirmation = WorkoutConfirmationFixture.defaultWorkoutConfirmation();
        List<WorkoutHistory> workoutHistories = List.of(
                WorkoutHistoryFixture.builder(worker, workoutConfirmation).build(),
                WorkoutHistoryFixture.builder(worker, workoutConfirmation).build(),
                WorkoutHistoryFixture.builder(worker, workoutConfirmation).build()
        );

        given(workoutHistoryRepository.findTodayByWorkerId(any())).willReturn(workoutHistories);

        // when, then
        assertThatThrownBy(() -> workoutValidator.validateCanWork(workspace, worker))
                .hasMessage(EXCEED_MAX_DAILY_WORKOUT_HISTORY_COUNT.getMessage());

    }

}
