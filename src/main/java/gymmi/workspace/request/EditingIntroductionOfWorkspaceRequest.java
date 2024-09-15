package gymmi.workspace.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class EditingIntroductionOfWorkspaceRequest {

    private String description;

    @Length(max = 10, message = "태그는 10자까지 가능합니다.")
    private String tag;

    public EditingIntroductionOfWorkspaceRequest(String description, String tag) {
        this.description = description;
        this.tag = tag;
    }
}
