package gymmi.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EditingDescriptionOfWorkspaceRequest {

    private String description;

    public EditingDescriptionOfWorkspaceRequest(String description) {
        this.description = description;
    }
}
