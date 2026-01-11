package gymmi.workspace.workout.service;

import gymmi.etc.domain.entity.User;
import gymmi.workspace.mission.domain.entity.Mission;
import gymmi.workspace.service.IntegrationTest;
import gymmi.workspace.workout.controller.request.WorkingMissionInWorkspaceRequest;
import gymmi.workspace.workout.controller.request.WorkoutRequest;
import gymmi.workspace.workspace.domain.WorkspaceStatus;
import gymmi.workspace.workspace.domain.entity.Worker;
import gymmi.workspace.workspace.domain.entity.Workspace;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class WorkoutServiceTest extends IntegrationTest {

//    @Test
//    void 워크스페이스_운동시_참여자의_점수가_반영되고_연동_여부에_따라_사진_피드가_등록_된다() {
//        // given
//        User user = persister.persistUser();
//        Workspace workspace = persister.persistWorkspace(user, WorkspaceStatus.IN_PROGRESS);
//        Worker worker = persister.persistWorker(user, workspace);
//        Mission mission = persister.persistMission(workspace, 10);
//        int count = 100;
//
//        List<WorkingMissionInWorkspaceRequest> requests = List.of(
//                new WorkingMissionInWorkspaceRequest(mission.getId(), count)
//        );
//        WorkoutRequest request = Instancio.of(WorkoutRequest.class)
//                .set(field(WorkoutRequest::getMissions), requests)
//                .set(field(WorkoutRequest::getWillLink), true)
//                .create();
//        assertThat(worker.getContributedScore()).isEqualTo(0);
//        given(s3Service.copy(any(), any(), any())).willReturn(UUID.randomUUID().toString());
//
//        // when
//        workoutService.workMissionsInWorkspace(user, workspace.getId(), request);
//
//        // then
//        assertThat(workoutHistoryRepository.getAllByWorkerId(worker.getId())).hasSize(1);
//        assertThat(worker.getContributedScore()).isEqualTo(mission.getScore() * count);
//        assertThat(workspace.getStatus()).isEqualTo(WorkspaceStatus.COMPLETED);
//        assertThat(assertThat(photoFeedRepository.findAll()).hasSize(1));
//    }

}
