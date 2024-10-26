package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gymmi.entity.User;
import gymmi.exception.message.ErrorCode;
import java.util.List;
import java.util.function.Function;
import org.instancio.Instancio;
import org.instancio.Random;
import org.instancio.Select;
import org.instancio.support.DefaultRandom;
import org.junit.jupiter.api.Test;

class WorkedTest {

    @Test
    void 운동점수가_반영된다() {
        // given
        Workspace workspace = Instancio.create(Workspace.class);
        User user = Instancio.create(User.class);
        Task task = Instancio.create(Task.class);
        List<WorkoutRecord> workoutRecords = Instancio.ofList(WorkoutRecord.class).size(2).create();
        Worker worker = new Worker(user, workspace, task);
        Worked worked = new Worked(worker, workoutRecords);
        assertThat(worker.getContributedScore()).isEqualTo(0);

        // when
        worked.apply();

        // then
        assertThat(worker.getContributedScore()).isEqualTo(worked.getSum());
    }

    @Test
    void 운동기록을_다른_워크스페이스에서_조회_하면_예외가_발생_한다() {
        // given
        Workspace workspace = Instancio.create(Workspace.class);
        Worker worker = Instancio.of(Worker.class)
                .set(Select.field(Worker::getWorkspace), workspace)
                .create();
        List<WorkoutRecord> workoutRecords = Instancio.ofList(WorkoutRecord.class).size(2).create();
        Worked worked = new Worked(worker, workoutRecords);
        Workspace workspace1 = Instancio.of(Workspace.class)
                .filter(Select.field(Workspace::getId), (Long id) -> !id.equals(workspace.getId()))
                .create();

        // when, then
        assertThatThrownBy(() -> worked.canBeReadIn(workspace1))
                .hasMessage(ErrorCode.NO_WORKOUT_HISTORY_EXIST_IN_WORKSPACE.getMessage());
    }

}
