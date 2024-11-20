package gymmi.photoboard.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePhotoFeedRequest {

    @NotBlank
    private String filename;

    @NotBlank
    private String comment;

    public CreatePhotoFeedRequest(String filename, String comment) {
        this.filename = filename;
        this.comment = comment;
    }
}
