package gymmi.workspace.repository;

import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.domain.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findAllByObjectionId(Long objectionId);

    boolean existsByObjectionIdAndWorkerId(Long objectionId, Long workerId);
}
