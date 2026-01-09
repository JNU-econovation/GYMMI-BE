package gymmi.etc.domain;

import gymmi.workspace.workspace.domain.WorkspaceCreationValidator;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspaceNameDuplicationCheck implements DuplicationCheck {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public boolean supports(DuplicationCheckType type) {
        return type == DuplicationCheckType.WORKSPACE_NAME;
    }

    @Override
    public boolean isDuplicate(String value) {
        WorkspaceCreationValidator.validateName(value);
        return workspaceRepository.existsByName(value);
    }
}
