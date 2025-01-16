package gymmi.global;

import gymmi.exceptionhandler.exception.NotMatchedException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.ObjectionStatus;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

public class StringToObejctionStatusConverter implements Converter<String, ObjectionStatus> {

    private final static Map<String, ObjectionStatus> queryParamValueMapping = new HashMap<>();

    public StringToObejctionStatusConverter() {
        queryParamValueMapping.put(ObjectionStatus.INCOMPLETION.name().toLowerCase(), ObjectionStatus.INCOMPLETION);
        queryParamValueMapping.put("inProgress", ObjectionStatus.IN_PROGRESS);
        queryParamValueMapping.put(ObjectionStatus.CLOSED.name().toLowerCase(), ObjectionStatus.CLOSED);
    }

    @Override
    public ObjectionStatus convert(String source) {
        if (queryParamValueMapping.containsKey(source)) {
            return queryParamValueMapping.get(source);
        }
        throw new NotMatchedException(ErrorCode.INVALID_OBJECTION_STATUS_VALUE);
    }
}
