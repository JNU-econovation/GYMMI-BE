package gymmi.controller;

import gymmi.entity.Feedback;
import gymmi.entity.User;
import gymmi.global.Logined;
import gymmi.repository.FeedbackRepository;
import gymmi.request.FeedbackRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackRepository feedbackRepository;


    @PostMapping("/feedback")
    public ResponseEntity<Void> feedback(
            @Logined User user,
            @RequestBody FeedbackRequest request
    ) {
        Feedback feedback = new Feedback(user, request.getContent());
        feedbackRepository.save(feedback);
        return ResponseEntity.ok().build();
    }


}
