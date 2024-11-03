package gymmi.service;

import gymmi.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    public PresignedUrlResponse getPresingedUrlWithPut() {
        return s3Client.generatePresignedUrlWithPut(ImageUse.WORKOUT_PROOF.getDirectory());
    }

    public String getPresignedUrl(String filename) {
        return s3Client.generatePresignedUrl(ImageUse.WORKOUT_PROOF.getDirectory(), filename);
    }

}
