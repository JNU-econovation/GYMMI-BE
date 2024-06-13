package gymmi.repository;


import gymmi.entity.Workspace;
import gymmi.exception.NotFoundResourcesException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("select ws from Workspace ws where ws.name = :name")
    Optional<Workspace> findWorkspaceByByName(String name);

    default Workspace getWorkspaceById(Long id) {
        Workspace workspace = findById(id)
                .orElseThrow(() -> new NotFoundResourcesException("해당 워크스페이스가 존재하지 않습니다."));
        return workspace;
    }
}
