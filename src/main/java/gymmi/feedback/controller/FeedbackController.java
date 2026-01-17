package gymmi.feedback.controller;

import gymmi.feedback.domain.Feedback;
import gymmi.user.domain.User;
import gymmi.global.common.resolver.Logined;
import gymmi.feedback.repository.FeedbackRepository;
import gymmi.feedback.controller.request.FeedbackRequest;
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
