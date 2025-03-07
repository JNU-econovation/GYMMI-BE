package gymmi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class EditingMyPageRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(min = 2, max = 5, message = "닉네임은 2~5자까지 가능합니다.")
    private String nickname;

    public EditingMyPageRequest(String nickname) {
        this.nickname = nickname;
    }
}
