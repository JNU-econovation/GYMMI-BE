package gymmi.photoboard.repository;

import gymmi.photoboard.domain.entity.ThumbsUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ThumbsUpRepository extends JpaRepository<ThumbsUp, Long> {


    @Query("select t from ThumbsUp t where t.user.id =:userId and t.photoFeed.id =:photoFeedId")
    Optional<ThumbsUp> findByUserIdAndPhotoFeedId(Long userId, Long photoFeedId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ThumbsUp t where t.photoFeed.id =:photoFeedId")
    void deleteByPhotoFeedId(Long photoFeedId);
}
