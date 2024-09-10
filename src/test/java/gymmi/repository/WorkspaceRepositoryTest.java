package gymmi.repository;

import gymmi.Fixtures;
import gymmi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

class WorkspaceRepositoryTest extends RepositoryTest {

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Test
    void 사용자가_참여한_워크스페이스_개수를_조회한다__완료된_워크스페이스는_제외() {
        // given

        // when
//        workspaceRepository.getCountsOfJoinedWorkspacesExcludeCompleted(null);
        // then

    }
}
