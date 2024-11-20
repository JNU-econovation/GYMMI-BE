package gymmi.photoboard.repository;

import gymmi.photoboard.domain.entity.ThumbsUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ThumbsUpRepository extends JpaRepository<ThumbsUp, Long> {


    @Query("select t from ThumbsUp t where t.user.id =:userId")
    Optional<ThumbsUp> findByUserId(Long userId);

}
