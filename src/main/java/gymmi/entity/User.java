package gymmi.entity;

import gymmi.exceptionhandler.exception.InvalidPatternException;
import gymmi.exceptionhandler.message.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

import static gymmi.utils.Regexpressions.*;

@Entity
@Table(name = "uuser")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Getter
public class User extends TimeEntity {

    private static final Pattern REGEX_LOGIN_ID = REGEX_영어_숫자_만;
    private static final Pattern REGEX_PASSWORD = Pattern.compile("^[a-zA-Z0-9" + SPECIAL_CHARACTER + "]+$");
    private static final Pattern REGEX_NICKNAME = REGEX_영어_한글_초성_숫자_만;
    private static final Pattern REGEX_SPECIAL_CHARACTER = Pattern.compile("[" + SPECIAL_CHARACTER + "]");
    private static final Pattern REGEX_EMAIL = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]*$");
    public static final String RESIGNED_NICKNAME = "(탈퇴한유저)";

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

    @Column(nullable = false)
    private boolean isResigned;

    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY)
    private ProfileImage profileImage; // 지연 로딩 안됨.

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private FcmToken fcmToken;

    @Builder
    public User(String loginId, String plainPassword, String nickname, String email) {
        validateLoginId(loginId);
        validatePassword(plainPassword);
        validateNickname(nickname);
        this.loginId = loginId;
        this.password = encryptPassword(plainPassword);
        this.nickname = nickname;
        this.email = validateEmail(email);
        this.isResigned = false;
    }

    private String validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return "";
        }
        return email;
    }

    private void validatePassword(String plainPassword) {
        if (!REGEX_PASSWORD.matcher(plainPassword).matches()) {
            throw new InvalidPatternException(ErrorCode.INVALID_PASSWORD_1);

        }
        if (!REGEX_영어.matcher(plainPassword).find()) {
            throw new InvalidPatternException(ErrorCode.INVALID_PASSWORD_2);
        }
        if (!REGEX_숫자.matcher(plainPassword).find()) {
            throw new InvalidPatternException(ErrorCode.INVALID_PASSWORD_3);
        }
        if (!REGEX_SPECIAL_CHARACTER.matcher(plainPassword).find()) {
            throw new InvalidPatternException(ErrorCode.INVALID_PASSWORD_4);
        }
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }


    public static void validateLoginId(String loginId) {
        if (!REGEX_LOGIN_ID.matcher(loginId).matches()) {
            throw new InvalidPatternException(ErrorCode.INVALID_LOGIN_ID_1);
        }
        if (!REGEX_영어.matcher(loginId).find()) {
            throw new InvalidPatternException(ErrorCode.INVALID_LOGIN_ID_2);
        }
        if (!REGEX_숫자.matcher(loginId).find()) {
            throw new InvalidPatternException(ErrorCode.INVALID_LOGIN_ID_3);
        }
    }

    public static String validateNickname(String nickname) {
        if (!REGEX_NICKNAME.matcher(nickname).matches()) {
            throw new InvalidPatternException(ErrorCode.INVALID_NICKNAME);
        }
        return nickname;
    }

    public boolean canAuthenticate(String loginId, String plainPassword) {
        if (this.loginId.equals(loginId) && BCrypt.checkpw(plainPassword, password)) {
            return true;
        }
        return false;
    }

    public boolean canAuthenticate(String plainPassword) {
        if (BCrypt.checkpw(plainPassword, password)) {
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

    public void resign() {
        this.isResigned = true;
        this.nickname = RESIGNED_NICKNAME;
    }

    public String getProfileImageName() {
        if (profileImage == null) {
            return ProfileImage.EMPTY_NAME;
        }
        return profileImage.getStoredName();
    }

    public String getLoginId() {
        return loginId;
    }

    public String getEmail() {
        return email;
    }

    public String getAlarmToken() {
        if (fcmToken == null) {
            return null;
        }
        return fcmToken.getToken();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", isResigned=" + isResigned +
                ", profileImage=" + profileImage +
                '}';
    }
}
