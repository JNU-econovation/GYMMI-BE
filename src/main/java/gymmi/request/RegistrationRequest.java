package gymmi.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank
    @Length(min = 6, max = 12)
    private String loginId;

    @NotBlank
    @Length(min = 2, max = 5)
    private String nickname;

    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    @Email
    private String email;

    @Builder
    public RegistrationRequest(String loginId, String nickname, String email, String password) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
