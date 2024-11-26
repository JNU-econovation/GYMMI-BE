package gymmi.photoboard.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.photoboard.request.CreatePhotoFeedRequest;
import gymmi.photoboard.response.PhotoFeedDetailResponse;
import gymmi.photoboard.response.PhotoFeedResponse;
import gymmi.photoboard.service.PhotoFeedService;
import gymmi.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhotoBoardController {

    private final PhotoFeedService photoFeedService;

    @PostMapping("/photos")
    public ResponseEntity<IdResponse> createPhotoFeed(
            @Logined User user,
            @Validated @RequestBody CreatePhotoFeedRequest request
    ) {
        Long id = photoFeedService.createPhotoFeed(user, request);
        return ResponseEntity.ok().body(new IdResponse(id));
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<PhotoFeedDetailResponse> seePhotoFeed(
            @Logined User user,
            @PathVariable(name = "photoId") Long photoFeedId
    ) {
        PhotoFeedDetailResponse response = photoFeedService.getPhotoFeed(user, photoFeedId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/photos")
    public ResponseEntity<List<PhotoFeedResponse>> seePhotoFeeds(
            @RequestParam(name = "pageNumber") int pageNumber
    ) {
        List<PhotoFeedResponse> responses = photoFeedService.getPhotoFeeds(pageNumber);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/photos/{photoId}/thumps-up")
    public ResponseEntity<Void> clickThumpsUp(
            @Logined User user,
            @PathVariable(name = "photoId") Long photoFeedId
    ) {
        photoFeedService.likePhotoFeed(user, photoFeedId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<Void> deletePhotoFeed(
            @Logined User user,
            @PathVariable(name = "photoId") Long photoFeedId
    ) {
        photoFeedService.delete(user, photoFeedId);
        return ResponseEntity.ok().build();
    }


}
