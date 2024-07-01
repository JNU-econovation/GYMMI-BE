package gymmi.service;

import gymmi.entity.Workspace;
import gymmi.global.DuplicationCheckType;
import gymmi.repository.WorkspaceRepository;
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
        Workspace.validateName(value);
        return workspaceRepository.findWorkspaceByByName(value).isPresent();
    }
}
