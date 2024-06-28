package gymmi;

import gymmi.entity.User;

public final class Fixtures {

    public static final String JSON_KEY_MESSAGE = "message";
    public static final String JSON_KEY_ERROR_CODE = "errorCode";
    public static final String JSON_KEY_ID = "id";
    public static final String JSON_KEY_ACCESS_TOKEN = "accessToken";
    public static final String JSON_KEY_REFRESH_TOKEN = "refreshToken";
    public static final String JSON_KEY_PASSWORD = "password";
    public static final String AUTHORIZATION_TYPE_BEARER = "Bearer ";


    public static final String USER__SATISFIED_LOGIN_ID = "test1234";
    public static final String USER__SATISFIED_NICKNAME = "지미지미";
    public static final String USER__SATISFIED_PASSWORD = "password1!";
    public static final User USER__DEFAULT_USER = User.builder()
            .loginId(USER__SATISFIED_LOGIN_ID)
            .plainPassword(USER__SATISFIED_PASSWORD)
            .nickname(USER__SATISFIED_NICKNAME)
            .email(null)
            .build();

    public static final String TASK__DEFAULT_TASK = "테스크입니다.";

    public static final String MISSION__SATISFIED_MISSION_NAME = "미션입니다.";
    public static final int MISSION__SATISFIED_MISSION_SCORE = 5;

    public static final String WORKSPACE__DISSATISFIED_PASSWORD = "!!!!";
    public static final String WORKSPACE__SATISFIED_NAME = "지미";
    public static final Integer WORKSPACE__SATISFIED_HEAD_COUNT = 2;
    public static final Integer WORKSPACE__SATISFIED_GOAL_SCORE = 500;

    private Fixtures() {
    }
}
