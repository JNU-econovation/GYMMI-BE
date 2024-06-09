package gymmi.utils;

import java.util.regex.Pattern;

public final class Regexpressions {

    public static final String SPECIAL_CHARACTER = "~!@#$%^&*()_+<>?:";

    public static final Pattern REGEX_영어_한글_숫자_만 = Pattern.compile("^[a-zA-Z가-힣0-9]+$");
    public static final Pattern REGEX_영어_숫자_만 = Pattern.compile("^[a-zA-Z0-9]+$");
    public static final Pattern REGEX_영어_한글_만 = Pattern.compile("^[a-zA-Z가-힣]+$");
    public static final Pattern REGEX_영어_한글_초성_숫자_만 = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$");
    public static final Pattern REGEX_영어 = Pattern.compile("[a-zA-Z]");
    public static final Pattern REGEX_숫자 = Pattern.compile("[0-9]");

    public static class User {

        public static final Pattern REGEX_EMAIL = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]*$");
        public static final Pattern REGEX_PASSWORD = Pattern.compile("^[a-zA-Z0-9" + SPECIAL_CHARACTER + "]+$");
        public static final Pattern REGEX_NICKNAME = REGEX_영어_한글_초성_숫자_만;
        public static final Pattern REGEX_LOGIN_ID = REGEX_영어_숫자_만;
        public static final Pattern REGEX_SPECIAL_CHARACTER = Pattern.compile("[" + SPECIAL_CHARACTER + "]");

    }

    public static class Workspace {
        public static final Pattern REGEX_WORKSPACE_NAME = REGEX_영어_한글_숫자_만;
        public static final Pattern REGEX_WORKSPACE_TAG = REGEX_영어_한글_만;

    }

}
