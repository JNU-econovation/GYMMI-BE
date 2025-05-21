package gymmi.global;

import gymmi.exceptionhandler.exception.NotMatchedException;
import gymmi.exceptionhandler.message.InvalidQueryParmErrorMessage;
import gymmi.workspace.domain.WorkspaceStatus;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class StringToWorkspaceStatusConverter implements Converter<String, WorkspaceStatus> {

    private final static Map<String, WorkspaceStatus> queryParamValueMapping = new HashMap<>();

    public StringToWorkspaceStatusConverter() {
        queryParamValueMapping.put("PREPARING", WorkspaceStatus.PREPARING);
        queryParamValueMapping.put("IN-PROGRESS", WorkspaceStatus.IN_PROGRESS);
        queryParamValueMapping.put("COMPLETED", WorkspaceStatus.COMPLETED);
    }

    @Override
    public WorkspaceStatus convert(String source) {
        if (queryParamValueMapping.containsKey(source)) {
            return queryParamValueMapping.get(source);
        }
        throw new NotMatchedException(InvalidQueryParmErrorMessage.INVALID_WORKSPACE_STATUS_VALUE);
    }
}
