package gymmi.service;

import gymmi.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public PresignedUrlResponse getPresingedUrlWithPut(ImageUse imageUse) {
        return s3Client.generatePresignedUrlWithPut(imageUse.getDirectory());
    }

    public String getPresignedUrl(ImageUse imageUse, String filename) {
        return s3Client.generatePresignedUrl(imageUse.getDirectory(), filename);
    }


}
