package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.entity.User;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.domain.entity.WorkoutConfirmation;

import java.util.List;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class WorkoutHistoryTest {

    @Test
    void 운동점수가_반영된다() {
        // given
        Workspace workspace = Instancio.create(Workspace.class);
        User user = Instancio.create(User.class);
        Task task = Instancio.create(Task.class);
        List<WorkoutRecord> workoutRecords = Instancio.ofList(WorkoutRecord.class).size(2).create();
        Worker worker = new Worker(user, workspace, task);
        WorkoutConfirmation workoutProof = Instancio.of(WorkoutConfirmation.class).create();
        WorkoutHistory workoutHistory = new WorkoutHistory(worker, workoutRecords, workoutProof);
        assertThat(worker.getContributedScore()).isEqualTo(0);

        // when
        workoutHistory.apply();

        // then
        assertThat(worker.getContributedScore()).isEqualTo(workoutHistory.getSum());
    }

    @Test
    void 운동기록을_다른_워크스페이스에서_조회_하면_예외가_발생_한다() {
        // given
        Workspace workspace = Instancio.create(Workspace.class);
        Worker worker = Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace)
                .create();
        List<WorkoutRecord> workoutRecords = Instancio.ofList(WorkoutRecord.class).size(2).create();
        WorkoutConfirmation workoutProof = Instancio.of(WorkoutConfirmation.class).create();
        WorkoutHistory workoutHistory = new WorkoutHistory(worker, workoutRecords, workoutProof);
        Workspace workspace1 = Instancio.of(Workspace.class)
                .filter(Select.field(Workspace::getId), (Long id) -> !id.equals(workspace.getId()))
                .create();

        // when, then
        assertThatThrownBy(() -> workoutHistory.canBeReadIn(workspace1))
                .hasMessage(ErrorCode.NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE.getMessage());
    }

}
