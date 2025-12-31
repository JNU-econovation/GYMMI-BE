package gymmi.eventlistener.event;

import gymmi.service.ImageUse;
import lombok.Getter;

@Getter
public class ImageValidationEvent {

    private final ImageUse imageUse;
    private final String filename;

    public ImageValidationEvent(ImageUse imageUse, String filename) {
        this.imageUse = imageUse;
        this.filename = filename;
    }
}
