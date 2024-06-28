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

}
