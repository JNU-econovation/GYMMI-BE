package gymmi.image.controller;

import gymmi.image.service.ImageService;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.photoboard.response.PhotoPresignedUrlResponse;
import gymmi.image.controller.response.PresignedUrlResponse;

import gymmi.workspace.workout.domain.entity.WorkoutConfirmation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/images/workout-proof/presignedUrl")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrlForWorkoutConfirmation(
    ) {
        PresignedUrlResponse response = imageService.getPresingedUrlWithPut(WorkoutConfirmation.IMAGE_USE);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/images/photo/presignedUrl")
    public ResponseEntity<PhotoPresignedUrlResponse> getPresignedUrlForPhoto(
    ) {
        PresignedUrlResponse presingedUrlResponse = imageService.getPresingedUrlWithPut(PhotoFeedImage.IMAGE_USE);
        PhotoPresignedUrlResponse response = new PhotoPresignedUrlResponse(presingedUrlResponse.getPresignedUrl(), presingedUrlResponse.getImageUrl());
        return ResponseEntity.ok().body(response);
    }

}
