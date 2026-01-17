package gymmi.workspace.workout.service;

import gymmi.workspace.service.IntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;

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
