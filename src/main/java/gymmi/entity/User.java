package gymmi.entity;

import static gymmi.utils.Regexpressions.REGEX_숫자;
import static gymmi.utils.Regexpressions.REGEX_영어;
import static gymmi.utils.Regexpressions.REGEX_영어_숫자_만;
import static gymmi.utils.Regexpressions.REGEX_영어_한글_초성_숫자_만;
import static gymmi.utils.Regexpressions.SPECIAL_CHARACTER;

import gymmi.exception.InvalidPatternException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

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

    @Column
    private String email;

    @Builder
    public User(String loginId, String plainPassword, String nickname, String email) {
        validateLoginId(loginId);
        validatePassword(plainPassword);
        validateNickname(nickname);
        this.loginId = loginId;
        this.password = encryptPassword(plainPassword);
        this.nickname = nickname;
        this.email = email;
    }

    private void validatePassword(String plainPassword) {
        if (!REGEX_PASSWORD.matcher(plainPassword).matches()) {
            throw new InvalidPatternException("비밀번호는 영문+숫자+특수문자 조합으로 구성해주세요.");
        }
        if (!REGEX_영어.matcher(plainPassword).find()) {
            throw new InvalidPatternException("영문을 포함해주세요");
        }
        if (!REGEX_숫자.matcher(plainPassword).find()) {
            throw new InvalidPatternException("숫자를 포함해주세요.");
        }
        if (!REGEX_SPECIAL_CHARACTER.matcher(plainPassword).find()) {
            throw new InvalidPatternException("특수문자를 포함해주세요.");
        }
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }


    private void validateLoginId(String loginId) {
        if (!REGEX_LOGIN_ID.matcher(loginId).matches()) {
            throw new InvalidPatternException("아이디는 영문+숫자 조합으로 구성해주세요.");
        }
        if (!REGEX_영어.matcher(loginId).find()) {
            throw new InvalidPatternException("영문을 포함해주세요");
        }
        if (!REGEX_숫자.matcher(loginId).find()) {
            throw new InvalidPatternException("숫자를 포함해주세요.");
        }
    }

    private void validateNickname(String nickname) {
        if (!REGEX_NICKNAME.matcher(nickname).matches()) {
            throw new InvalidPatternException("닉네임은 한글(초성), 영문, 숫자만 가능합니다.");
        }
    }

    public boolean canAuthenticate(String loginId, String plainPassword) {
        if (this.loginId.equals(loginId) && BCrypt.checkpw(plainPassword, password)) {
            return true;
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
