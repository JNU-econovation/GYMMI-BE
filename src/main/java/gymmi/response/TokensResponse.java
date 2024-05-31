package gymmi.response;

import lombok.Getter;

@Getter
public class TokensResponse {

    private final String accessToken;
    private final String refreshToken;

    public TokensResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
