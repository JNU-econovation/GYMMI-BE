package gymmi.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackRequest {

    @NotBlank
    private String content;

}
