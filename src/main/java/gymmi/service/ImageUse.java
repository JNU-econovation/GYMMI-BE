package gymmi.service;

import lombok.Getter;

@Getter
public enum ImageUse {
    WORKOUT_PROOF("workout_proof/"),
    PROFILE(""),
    COMMUNITY("");

    private final String directory;

    ImageUse(String directory) {
        this.directory = directory;
    }

}
