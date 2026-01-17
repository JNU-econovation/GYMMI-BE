package gymmi.user.controller;

import gymmi.user.domain.User;
import gymmi.global.common.resolver.Logined;
import gymmi.user.controller.request.EditingMyPageRequest;
import gymmi.user.controller.response.MyPageResponse;
import gymmi.user.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @PutMapping(value = "/my/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> editProfileImage(
            @Logined User user,
            @RequestParam("profileImage") MultipartFile profileImageFile
    ) {
        myPageService.setProfileImage(user, profileImageFile);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/my/profile-image")
    public ResponseEntity<Void> deleteProfileImage(
            @Logined User user
    ) {
        myPageService.deleteProfileImage(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/my/nickname/edit")
    public ResponseEntity<Void> editMyPage(
            @Logined User user,
            @RequestBody @Validated EditingMyPageRequest request
    ) {
        myPageService.editMyPage(user, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<MyPageResponse> seeMyPage(
            @Logined User user
    ) {
        MyPageResponse response = myPageService.getMyInfo(user);
        return ResponseEntity.ok().body(response);
    }


}
