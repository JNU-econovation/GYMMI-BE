package gymmi.photoboard.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.photoboard.service.PhotoFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PhotoBoardController {

    private final PhotoFeedService photoFeedService;

    @PostMapping("/photos")
    public ResponseEntity<Void> createPhotoFeed(
            @Logined User user,
            @Validated @RequestBody CreatePhotoFeedRequest request
    ) {
        photoFeedService.createPhotoFeed(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<PhotoFeedResponse> seePhotoFeed(
            @PathVariable(name = "photoId") Long photoFeedId
    ) {
        PhotoFeedResponse response = photoFeedService.getPhotoFeed(photoFeedId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/photos/{photoId}/thumps-up")
    public ResponseEntity<Void> clickThumpsUp(
            @Logined User user,
            @PathVariable(name = "photoId") Long photoFeedId
    ) {
        photoFeedService.likePhotoFeed(user, photoFeedId);
        return ResponseEntity.ok().build();
    }


}
