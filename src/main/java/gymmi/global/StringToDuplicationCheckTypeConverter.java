package gymmi.global;

import gymmi.exceptionhandler.exception.NotMatchedException;
import gymmi.exceptionhandler.message.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

public class StringToDuplicationCheckTypeConverter implements Converter<String, DuplicationCheckType> {

    private final static Map<String, DuplicationCheckType> queryParamValueMapping = new HashMap<>();

    public StringToDuplicationCheckTypeConverter() {
        queryParamValueMapping.put("LOGIN-ID", DuplicationCheckType.LOGIN_ID);
        queryParamValueMapping.put("WORKSPACE-NAME", DuplicationCheckType.WORKSPACE_NAME);
        queryParamValueMapping.put("NICKNAME", DuplicationCheckType.NICKNAME);
    }

    @Override
    public DuplicationCheckType convert(String source) {
        if (queryParamValueMapping.containsKey(source)) {
            return queryParamValueMapping.get(source);
        }
        throw new NotMatchedException(ErrorCode.INVALID_WORKSPACE_STATUS_VALUE);
    }
}
