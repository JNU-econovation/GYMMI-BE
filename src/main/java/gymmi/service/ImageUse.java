package gymmi.service;

import lombok.Getter;

@Getter
public enum ImageUse {
    WORKOUT_CONFIRMATION("workout_proof/"),
    PROFILE(""),
    PHOTO_FEED("photo_feed/");

    private final String directory;

    ImageUse(String directory) {
        this.directory = directory;
    }

}
