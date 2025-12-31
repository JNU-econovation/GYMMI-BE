package gymmi.eventlistener;

import gymmi.eventlistener.event.ImageValidationEvent;
import gymmi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageValidationEventListener {

    private final S3Service s3Service;

    @EventListener
    public void validate(ImageValidationEvent event) {
        s3Service.validateObjectPresence(event.getImageUse(), event.getFilename());
    }

}
