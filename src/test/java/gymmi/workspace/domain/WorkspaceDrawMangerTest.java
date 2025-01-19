package gymmi.workspace.domain;

import gymmi.workspace.domain.entity.Worker;
import gymmi.workspace.domain.entity.Workspace;
import gymmi.workspace.domain.entity.WorkspaceResult;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static gymmi.exceptionhandler.message.ErrorCode.NOT_COMPLETED_WORKSPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkspaceDrawMangerTest {

    @Test
    void 워크스페이스가_종료되지_않은_경우_예외가_발생한다() {
        // given
        Workspace workspace = Instancio.of(Workspace.class)
                .set(Select.field(Workspace::getStatus), WorkspaceStatus.IN_PROGRESS)
                .create();

        List<Worker> workers = Instancio.ofList(Worker.class)
                .size(8)
                .create();

        // when, then
        assertThatThrownBy(() -> new WorkspaceDrawManger(workspace, workers))
                .hasMessage(NOT_COMPLETED_WORKSPACE.getMessage());
    }

    @Nested
    class 뽑기 {
        @Test
        void 일등과_꼴등을_뽑은후_결과를_적용한다면_워크스페이스는_완전히_종료된다() {
            // given
            Workspace workspace = Instancio.of(Workspace.class)
                    .set(Select.field(Workspace::getStatus), WorkspaceStatus.COMPLETED)
                    .create();

            List<Worker> workers = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Worker worker = Instancio.of(Worker.class)
                        .set(Select.field(Worker::getContributedScore), i)
                        .create();
                workers.add(worker);
            }
            WorkspaceDrawManger workspaceDrawManger = new WorkspaceDrawManger(workspace, workers);
            WorkspaceResult result = workspaceDrawManger.draw();
            assertThat(workspace.isFullyCompleted()).isFalse();

            // when
            result.apply();

            // then
            assertThat(workspace.isFullyCompleted()).isTrue();
            assertThat(result.getWinner()).isEqualTo(workers.get(4));
            assertThat(result.getLoser()).isEqualTo(workers.get(0));
        }

        @Test
        void 동점인_경우_랜덤으로_뽑는다() {
            // given
            Workspace workspace = Instancio.of(Workspace.class)
                    .set(Select.field(Workspace::getStatus), WorkspaceStatus.COMPLETED)
                    .create();

            List<Worker> workers = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Worker worker = Instancio.of(Worker.class)
                        .set(Select.field(Worker::getContributedScore), i)
                        .create();
                workers.add(worker);
            }
            for (int i = 0; i < 5; i++) {
                Worker worker = Instancio.of(Worker.class)
                        .set(Select.field(Worker::getContributedScore), i)
                        .create();
                workers.add(worker);
            }
            WorkspaceDrawManger workspaceDrawManger = new WorkspaceDrawManger(workspace, workers);

            // when
            List<WorkspaceResult> results = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                WorkspaceResult result = workspaceDrawManger.draw();
                results.add(result);
            }
            List<Worker> winners = results.stream().map(WorkspaceResult::getWinner).toList();
            System.out.println("winners = " + winners);
            List<Worker> losers = results.stream().map(WorkspaceResult::getLoser).toList();
            System.out.println("losers = " + losers);

            // then
            assertThat(winners).containsAnyOf(workers.get(4), workers.get(9));
            assertThat(losers).containsAnyOf(workers.get(0), workers.get(5));
        }

    }
}
