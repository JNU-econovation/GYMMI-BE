package gymmi.controller;

import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
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

}
