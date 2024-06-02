package gymmi.global;

import org.springframework.core.convert.converter.Converter;

public class StringToDuplicationCheckType implements Converter<String, DuplicationCheckType> {

    @Override
    public DuplicationCheckType convert(String source) {
        return DuplicationCheckType.findBy(source.toUpperCase());
    }
}
