package gymmi.response;

import lombok.Getter;

@Getter
public class PresignedUrlResponse {

    private final String presignedUrl;
    private final String imageUrl;

    public PresignedUrlResponse(String presignedUrl, String filename) {
        this.presignedUrl = presignedUrl;
        this.imageUrl = filename;
    }
}
