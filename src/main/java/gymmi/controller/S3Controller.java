package gymmi.controller;

import gymmi.response.PresignedUrlResponse;
import gymmi.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/images/workout-proof/presignedUrl")
    public ResponseEntity<PresignedUrlResponse> s3(
    ) {
        PresignedUrlResponse response = s3Service.getPresingedUrlWithPut();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/images/workout-proof/presignedUrl/get")
    public ResponseEntity<String> s3(
            @RequestParam("imageUrl") String filenmae
    ) {
        String presignedUrl = s3Service.getPresignedUrl(filenmae);
        return ResponseEntity.ok().body(presignedUrl);
    }


}
