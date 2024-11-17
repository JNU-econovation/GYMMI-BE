package gymmi.photoboard.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.photoboard.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PhotoBoardController {

    private final PhotoService photoService;

    @PostMapping("/photos")
    public ResponseEntity<Void> createPhotoFeed(
            @Logined User user,
            @Validated @RequestBody CreatePhotoFeedRequest request
    ) {
        photoService.createPhotoFeed(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<PhotoFeedResponse> seePhotoFeed(
            @PathVariable(name = "photoId") Long photoFeedId
    ) {
        PhotoFeedResponse response = photoService.getPhotoFeed(photoFeedId);
        return ResponseEntity.ok().body(response);
    }


}
