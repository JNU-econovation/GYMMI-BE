package gymmi.controller;

import gymmi.photoboard.response.PhotoPresignedUrlResponse;
import gymmi.response.PresignedUrlResponse;
import gymmi.service.ImageUse;
import gymmi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/images/workout-proof/presignedUrl")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrlForWorkoutConfirmation(
    ) {
        PresignedUrlResponse response = s3Service.getPresingedUrlWithPut(ImageUse.WORKOUT_CONFIRMATION);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/images/photo/presignedUrl")
    public ResponseEntity<PhotoPresignedUrlResponse> getPresignedUrlForPhoto(
    ) {
        PresignedUrlResponse presingedUrlResponse = s3Service.getPresingedUrlWithPut(ImageUse.PHOTO_FEED);
        PhotoPresignedUrlResponse response = new PhotoPresignedUrlResponse(presingedUrlResponse.getPresignedUrl(), presingedUrlResponse.getImageUrl());
        return ResponseEntity.ok().body(response);
    }

}
