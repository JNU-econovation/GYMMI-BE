package gymmi.workspace.domain;

import gymmi.user.domain.User;
import gymmi.fixture.UserFixture;
import gymmi.fixture.WorkerFixture;
import gymmi.fixture.WorkspaceFixture;
import gymmi.global.exception.message.ErrorCode;
import gymmi.workspace.workspace.domain.LeftWorker;
import gymmi.workspace.workspace.domain.WorkspacePreparingManager;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkspacePreparingManagerTest {

    @Nested
    class 워크스페이스_참여_허락 {

        @Test
        void 사용자에게_워크스페이스_참가를_허락하면_새로운_워커가_반환된다() {
            // given
            User user = UserFixture.defaultUser();
            Workspace workspace = WorkspaceFixture.builder(user).workspaceStatus(WorkspaceStatus.PREPARING).build();
            Worker worker = WorkerFixture.builder(user, workspace).build();
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, new ArrayList<>(List.of(worker)));

            User user1 = UserFixture.builder().id(1L).build();

            // when
            Worker newWorker = workspacePreparingManager.allow(user1, workspace.getPassword());

            // then
            assertThat(workspacePreparingManager.getWorkers()).hasSize(2);
            assertThat(newWorker.getWorkspace()).isEqualTo(workspace);
        }

        @Test
        void 워크스페이스_비밀번호가_일치하지_않는_경우_예외가_발생한다() {
            // given
            User user = UserFixture.defaultUser();
            Workspace workspace = WorkspaceFixture.builder(user).password("1234").build();
            Worker worker = WorkerFixture.builder(user, workspace).build();
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, new ArrayList<>(List.of(worker)));

            User user1 = UserFixture.builder().id(1L).build();
            String wrongPassword = "1235";

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user1, wrongPassword))
                    .hasMessage(ErrorCode.NOT_MATCHED_PASSWORD.getMessage());
        }

        @Test
        void 워크스페이스_인원이_가득_찬_경우_예외가_발생한다() {
            // given
            User user = UserFixture.defaultUser();
            User user1 = UserFixture.builder().id(1L).build();

            Workspace workspace = WorkspaceFixture.builder(user).headCount(2).build();
            Worker worker = WorkerFixture.builder(user, workspace).build();
            Worker worker1 = WorkerFixture.builder(user1, workspace).build();
            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(
                    workspace, new ArrayList<>(List.of(worker, worker1))
            );

            User user2 = UserFixture.builder().id(2L).build();

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user2, workspace.getPassword()))
                    .hasMessage(ErrorCode.FULL_WORKSPACE.getMessage());
        }

        @Test
        void 워크스페이스에_이미_참여_한_경우_예외가_발생_한다() {
            // given
            User user = UserFixture.defaultUser();
            Workspace workspace = WorkspaceFixture.builder(user).build();
            Worker worker = WorkerFixture.builder(user, workspace).build();

            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, new ArrayList<>(List.of(worker)));

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.allow(user, workspace.getPassword()))
                    .hasMessage(ErrorCode.ALREADY_JOINED_WORKSPACE.getMessage());
        }
    }

    @Nested
    class 워크스페이스_참여자_내보내기 {

        @Test
        void 워크스페이스_참여자가_아닌_경우_예외가_발생한다() {
            User user = UserFixture.defaultUser();
            Workspace workspace = WorkspaceFixture.builder(user).build();
            Worker worker = WorkerFixture.builder(user, workspace).build();

            User user1 = UserFixture.builder().id(1L).build();
            Workspace workspace1 = WorkspaceFixture.builder(user1).id(1L).build();
            Worker worker1 = WorkerFixture.builder(user1, workspace1).id(1L).build();

            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, new ArrayList<>(List.of(worker)));

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.release(worker1))
                    .hasMessage(ErrorCode.NOT_JOINED_WORKSPACE.getMessage());
        }

        @Test
        void 방장이_워크스페이스_참여자가_남아있을때_나가는_경우_예외가_발생한다() {
            // given
            User user = UserFixture.defaultUser();
            Workspace workspace = WorkspaceFixture.builder(user).build();
            Worker creator = WorkerFixture.builder(user, workspace).build();

            User user1 = UserFixture.builder().id(1L).build();
            Worker worker1 = WorkerFixture.builder(user1, workspace).id(1L).build();

            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, new ArrayList<>(List.of(creator, worker1)));

            // when, then
            assertThatThrownBy(() -> workspacePreparingManager.release(creator))
                    .hasMessage(ErrorCode.EXIST_WORKERS_EXCLUDE_CREATOR.getMessage());
        }

        @Test
        void 워크스페이스_마지막_남은_참여자를_내보낸다() {
            // given
            User user = UserFixture.defaultUser();
            Workspace workspace = WorkspaceFixture.builder(user).build();
            Worker creator = WorkerFixture.builder(user, workspace).build();

            WorkspacePreparingManager workspacePreparingManager = new WorkspacePreparingManager(workspace, new ArrayList<>(List.of(creator)));

            // when
            LeftWorker result = workspacePreparingManager.release(creator);

            // then
            assertThat(workspacePreparingManager.getWorkers()).isEmpty();
            assertThat(result.getWorker()).isEqualTo(creator);
            assertThat(result.isLastLeaver()).isTrue();
        }
    }

}
