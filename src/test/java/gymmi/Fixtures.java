package gymmi;

import gymmi.entity.User;

public final class Fixtures {

    // 클래스 이름_..._...

    public static final String JSON_KEY_MESSAGE = "message";
    public static final String JSON_KEY_ERROR_CODE = "errorCode";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_ACCESS_TOKEN = "accessToken";
    public static final String JSON_KEY_REFRESH_TOKEN = "refreshToken";
    public static final String JSON_KEY_PASSWORD = "password";

    public static final String PARAMETER_KEY_TYPE = "type";
    public static final String PARAMETER_KEY_VALUE = "value";
    public static final String AUTHORIZATION_TYPE_BEARER = "Bearer ";

    public static final String SATISFIED_LOGIN_ID = "test1234";
    public static final String SATISFIED_NICKNAME = "지미지미";
    public static final String SATISFIED_PASSWORD = "password1!";

    public static final String DEFAULT_TASK = "테스크입니다.";

    public static final String SATISFIED_MISSION_NAME = "미션입니다.";
    public static final int SATISFIED_MISSION_SCORE = 5;

    public static final User USER_DEFAULT = User.builder()
            .loginId(SATISFIED_LOGIN_ID)
            .plainPassword(SATISFIED_PASSWORD)
            .nickname(SATISFIED_NICKNAME)
            .email(null)
            .build();


    public static final String WORKSPACE_DISSATISFIED_PASSWORD = "!!!!";

    public static final class Workspace {
        public static final String SATISFIED_NAME = "지미";
        public static final Integer SATISFIED_HEAD_COUNT = 2;
        public static final Integer SATISFIED_GOAL_SCORE = 500;

    }


    private Fixtures() {
    }
}
