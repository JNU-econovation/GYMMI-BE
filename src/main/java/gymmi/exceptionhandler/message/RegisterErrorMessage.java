package gymmi.exceptionhandler.message;

public enum RegisterErrorMessage implements ErrorMessage {

    // 회원가입
    ALREADY_USED_LOGIN_ID("이미 사용중인 아이디 입니다.", 400),
    ALREADY_USED_NICKNAME("이미 사용중인 닉네임 입니다.", 400),
    INVALID_LOGIN_ID_1("아이디는 영문+숫자 조합으로 구성해주세요.", 400),
    INVALID_LOGIN_ID_2("아이디에 영문을 포함해주세요", 400),
    INVALID_LOGIN_ID_3("아이디에 숫자를 포함해주세요.", 400),
    INVALID_PASSWORD_1("비밀번호는 영문+숫자+특수문자 조합으로 구성해주세요.", 400),
    INVALID_PASSWORD_2("비밀번호에 영문을 포함해주세요", 400),
    INVALID_PASSWORD_3("비밀번호에 숫자를 포함해주세요.", 400),
    INVALID_PASSWORD_4("비밀번호에 특수문자를 포함해주세요.", 400),
    INVALID_NICKNAME("닉네임은 한글(초성), 영문, 숫자만 가능합니다.", 400),
    ;

    private final String message;
    private final int statusCode;

    RegisterErrorMessage(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
