package gymmi.response;

import lombok.Getter;

@Getter
public class IdResponse {

    private final Long id;

    public IdResponse(Long id) {
        this.id = id;
    }
}
