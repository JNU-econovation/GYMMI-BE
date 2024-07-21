package gymmi.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MyPageResponse {

    private final String profileImage;
    private final String nickname;
    private final String loginId;
    private final String email;

    @Builder
    public MyPageResponse(String profileImage, String nickname, String loginId, String email) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.loginId = loginId;
        this.email = email;
    }
}
