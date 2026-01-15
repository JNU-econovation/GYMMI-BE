package gymmi.global.common.eventlistener;

import gymmi.global.common.eventlistener.event.ImageValidationEvent;

import gymmi.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageValidationEventListener {

    private final ImageService imageService;

    @EventListener
    public void validate(ImageValidationEvent event) {
        imageService.validateObjectPresence(event.getImageUse(), event.getFilename());
    }

}
