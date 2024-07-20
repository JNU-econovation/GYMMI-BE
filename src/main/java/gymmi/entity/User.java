package gymmi.entity;

import gymmi.exception.InvalidPatternException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

import static gymmi.utils.Regexpressions.*;

@Entity
@Table(name = "uuser")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class User {

    private static final Pattern REGEX_LOGIN_ID = REGEX_영어_숫자_만;
    private static final Pattern REGEX_PASSWORD = Pattern.compile("^[a-zA-Z0-9" + SPECIAL_CHARACTER + "]+$");
    private static final Pattern REGEX_NICKNAME = REGEX_영어_한글_초성_숫자_만;
    private static final Pattern REGEX_SPECIAL_CHARACTER = Pattern.compile("[" + SPECIAL_CHARACTER + "]");
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

    @Column(nullable = false)
    @ColumnDefault("''")
    private String email;

    @Builder
    public User(String loginId, String plainPassword, String nickname, String email) {
        validateLoginId(loginId);
        validatePassword(plainPassword);
        validateNickname(nickname);
        this.loginId = loginId;
        this.password = encryptPassword(plainPassword);
        this.nickname = nickname;
        this.email = validateEmail(email);
    }

    private String validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return "";
        }
        return email;
    }

    private void validatePassword(String plainPassword) {
        if (!REGEX_PASSWORD.matcher(plainPassword).matches()) {
            throw new InvalidPatternException("비밀번호는 영문+숫자+특수문자 조합으로 구성해주세요.");
        }
        if (!REGEX_영어.matcher(plainPassword).find()) {
            throw new InvalidPatternException("비밀번호에 영문을 포함해주세요");
        }
        if (!REGEX_숫자.matcher(plainPassword).find()) {
            throw new InvalidPatternException("비밀번호에 숫자를 포함해주세요.");
        }
        if (!REGEX_SPECIAL_CHARACTER.matcher(plainPassword).find()) {
            throw new InvalidPatternException("비밀번호에 특수문자를 포함해주세요.");
        }
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }


    public static void validateLoginId(String loginId) {
        if (!REGEX_LOGIN_ID.matcher(loginId).matches()) {
            throw new InvalidPatternException("아이디는 영문+숫자 조합으로 구성해주세요.");
        }
        if (!REGEX_영어.matcher(loginId).find()) {
            throw new InvalidPatternException("아이디에 영문을 포함해주세요");
        }
        if (!REGEX_숫자.matcher(loginId).find()) {
            throw new InvalidPatternException("아이디에 숫자를 포함해주세요.");
        }
    }

    public static String validateNickname(String nickname) {
        if (!REGEX_NICKNAME.matcher(nickname).matches()) {
            throw new InvalidPatternException("닉네임은 한글(초성), 영문, 숫자만 가능합니다.");
        }
        return nickname;
    }

    public boolean canAuthenticate(String loginId, String plainPassword) {
        if (this.loginId.equals(loginId) && BCrypt.checkpw(plainPassword, password)) {
            return true;
        }
        return false;
    }

    public void changeNickname(String nickname) {
        this.nickname = validateNickname(nickname);
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
