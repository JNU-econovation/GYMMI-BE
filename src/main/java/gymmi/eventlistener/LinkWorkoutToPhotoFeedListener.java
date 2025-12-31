package gymmi.eventlistener;

import gymmi.entity.User;
import gymmi.eventlistener.event.LinkToPhotoFeedEvent;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.service.PhotoFeedService;
import gymmi.repository.UserRepository;
import gymmi.service.ImageUse;
import gymmi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Async
public class LinkWorkoutToPhotoFeedListener {

    private final S3Service s3Service;
    private final PhotoFeedService photoFeedService;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void upload(LinkToPhotoFeedEvent event) {
        User user = userRepository.findByIdOrThrow(event.getUserId());
        String filename = s3Service.copy(ImageUse.WORKOUT_CONFIRMATION, event.getImageUrl(), ImageUse.PHOTO_FEED);
        photoFeedService.createPhotoFeed(user, new CreatePhotoFeedRequest(filename, event.getComment()));
    }
}
