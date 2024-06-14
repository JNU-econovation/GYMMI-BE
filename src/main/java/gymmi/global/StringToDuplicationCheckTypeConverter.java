package gymmi.global;

import gymmi.exception.NotFoundResourcesException;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

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
        throw new NotFoundResourcesException("해당 type은 존재하지 않는 type 입니다.");
    }
}
