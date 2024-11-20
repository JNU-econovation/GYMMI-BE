package gymmi.photoboard.response;

import lombok.Getter;

@Getter
public class PhotoPresignedUrlResponse {

    private final String presignedUrl;
    private final String filename;

    public PhotoPresignedUrlResponse(String presignedUrl, String filename) {
        this.presignedUrl = presignedUrl;
        this.filename = filename;
    }
}
