package gymmi.workspace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import gymmi.entity.User;
import java.util.List;
import org.instancio.Instancio;
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

}
