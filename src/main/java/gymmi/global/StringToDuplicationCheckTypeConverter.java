package gymmi.global;

import gymmi.exception.NotFoundResourcesException;
import gymmi.exception.NotMatchedException;
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
        throw new NotMatchedException("잘못된 query param value 입니다. : " + source);
    }
}
