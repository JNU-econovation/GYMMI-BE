package gymmi.response;


import lombok.Getter;

@Getter
public class DuplicationResponse {

    private final boolean duplication;

    public DuplicationResponse(boolean duplication) {
        this.duplication = duplication;
    }
}
