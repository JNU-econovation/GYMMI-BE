package gymmi.entity;

import gymmi.exception.InvalidPatternException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "uuser")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private static final Pattern REGEX_LOGIN_ID = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern REGEX_NICKNAME = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$");
    private static final Pattern REGEX_EMAIL = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]*$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String email;

    @Builder
    public User(String loginId, String plainPassword, String nickname, String email) {
        validateLoginId(loginId);
        validateNickname(nickname);
        this.loginId = loginId;
        this.password = encryptPassword(plainPassword);
        this.nickname = nickname;
        this.email = email;
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }


    private void validateLoginId(String loginId) {
        Matcher matcher = REGEX_LOGIN_ID.matcher(loginId);
        if (!matcher.matches()) {
            throw new InvalidPatternException("아이디는 영문과 숫자만 가능합니다.");
        }
    }

    private void validateNickname(String nickname) {
        Matcher matcher = REGEX_NICKNAME.matcher(nickname);
        if (!matcher.matches()) {
            throw new InvalidPatternException("닉네임은 한글(초성), 영문, 숫자만 가능합니다.");
        }
    }
}
