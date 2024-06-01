package gymmi.global;

import gymmi.exception.NotFoundResourcesException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

public enum DuplicationCheckType {
    LOGIN_ID("LOGIN-ID"),
    WORKSPACE_NAME("WORKSPACE-NAME"),
    NICKNAME("NICKNAME"),
    ;

    private final String key;

    DuplicationCheckType(String key) {
        this.key = key;
    }

    public static DuplicationCheckType findBy(String key) {
        return Arrays.stream(values())
                .filter(type -> type.hasKey(key))
                .findAny()
                .orElseThrow(() -> new NotFoundResourcesException("존재하지 않는 type 입니다."));
    }

    public DuplicationCheckType s(String name) {
        return valueOf(name);
    }

    public boolean hasKey(String key) {
        return this.key.equals(key);
    }
}
