package gymmi.repository;


import gymmi.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("select ws from Workspace ws where ws.name = :name")
    Optional<Workspace> findWorkspaceByByName(String name);
}
