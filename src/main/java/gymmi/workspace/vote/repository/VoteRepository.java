package gymmi.workspace.vote.repository;

import gymmi.workspace.vote.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findAllByObjectionId(Long objectionId);

    boolean existsByObjectionIdAndWorkerId(Long objectionId, Long workerId);
}
