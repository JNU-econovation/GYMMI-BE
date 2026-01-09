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

    @NotBlank(message = "아이디를 입력해주세요.")
    @Length(min = 6, max = 12, message = "아이디는 6~12자까지 가능합니다.")
    private String loginId;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(min = 2, max = 5, message = "닉네임은 2~5자까지 가능합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 20, message = "비밀번호는 8~20자까지 가능합니다.")
    private String password;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Builder
    public RegistrationRequest(String loginId, String nickname, String email, String password) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
