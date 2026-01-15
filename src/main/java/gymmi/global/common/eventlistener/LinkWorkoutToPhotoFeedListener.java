package gymmi.global.common.eventlistener;

import gymmi.image.service.ImageService;
import gymmi.user.domain.User;
import gymmi.global.common.eventlistener.event.LinkToPhotoFeedEvent;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.service.PhotoFeedService;
import gymmi.user.repository.UserRepository;
import gymmi.image.domain.ImageUse;

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

    private final ImageService imageService;
    private final PhotoFeedService photoFeedService;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void upload(LinkToPhotoFeedEvent event) {
        User user = userRepository.findByIdOrThrow(event.getUserId());
        String filename = imageService.copy(ImageUse.WORKOUT_CONFIRMATION, event.getImageUrl(), ImageUse.PHOTO_FEED);
        photoFeedService.createPhotoFeed(user, new CreatePhotoFeedRequest(filename, event.getComment()));
    }
}
