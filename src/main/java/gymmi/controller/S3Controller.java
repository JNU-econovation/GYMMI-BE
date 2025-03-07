package gymmi.controller;

import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.response.PhotoPresignedUrlResponse;
import gymmi.response.PresignedUrlResponse;
import gymmi.service.S3Service;
import gymmi.workspace.domain.entity.WorkoutConfirmation;
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
        PresignedUrlResponse response = s3Service.getPresingedUrlWithPut(WorkoutConfirmation.IMAGE_USE);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/images/photo/presignedUrl")
    public ResponseEntity<PhotoPresignedUrlResponse> getPresignedUrlForPhoto(
    ) {
        PresignedUrlResponse presingedUrlResponse = s3Service.getPresingedUrlWithPut(PhotoFeedImage.IMAGE_USE);
        PhotoPresignedUrlResponse response = new PhotoPresignedUrlResponse(presingedUrlResponse.getPresignedUrl(), presingedUrlResponse.getImageUrl());
        return ResponseEntity.ok().body(response);
    }

}
