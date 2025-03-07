package gymmi.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Long userId;
    private final String nickname;
    private final String profileURL;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    public LoginResponse(Long userId, String nickname, String profileURL, String accessToken, String refreshToken) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileURL = profileURL;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
