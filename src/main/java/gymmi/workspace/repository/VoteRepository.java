package gymmi.workspace.repository;

import gymmi.workspace.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
