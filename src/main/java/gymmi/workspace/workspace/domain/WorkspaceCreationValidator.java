package gymmi.workspace.workspace.domain;

import gymmi.exceptionhandler.exception.AlreadyExistException;
import gymmi.exceptionhandler.exception.InvalidNumberException;
import gymmi.exceptionhandler.exception.InvalidPatternException;
import gymmi.exceptionhandler.exception.InvalidRangeException;
import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

import static gymmi.utils.Regexpressions.REGEX_영어_한글_숫자_만;
import static gymmi.utils.Regexpressions.REGEX_영어_한글_쉼표_만;

@Component
@RequiredArgsConstructor
public class WorkspaceCreationValidator {

    public static final int MIN_GOAL_SCORE = 100;
    public static final int MAX_GOAL_SCORE = 1000;

    public static final int MIN_HEAD_COUNT = 2;
    public static final int MAX_HEAD_COUNT = 9;

    private static final Pattern REGEX_WORKSPACE_NAME = REGEX_영어_한글_숫자_만;
    private static final Pattern REGEX_WORKSPACE_TAG = REGEX_영어_한글_쉼표_만;
    public static final int MAX_MISSION_COUNT = 15;

    private final WorkspaceRepository workspaceRepository;

    public static Integer validateHeadCount(Integer headCount) {
        if (headCount < MIN_HEAD_COUNT || headCount > MAX_HEAD_COUNT) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_HEAD_COUNT);
        }
        return headCount;
    }

    public static String validateName(String name) {
        if (name.length() > 9) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_NAME_LENGTH);
        }
        if (!REGEX_WORKSPACE_NAME.matcher(name).matches()) {
            throw new InvalidPatternException(ErrorCode.INVALID_WORKSPACE_NAME_FORMAT);
        }
        return name;
    }

    public static Integer validateGoalScore(Integer goalScore) {
        if (goalScore < MIN_GOAL_SCORE || goalScore > MAX_GOAL_SCORE) {
            throw new InvalidRangeException(ErrorCode.INVALID_WORKSPACE_GOAL_SCORE);
        }

        if (!(goalScore % 10 == 0)) {
            throw new InvalidNumberException(ErrorCode.INVALID_MISSION_SCORE_UNIT);
        }
        return goalScore;
    }

    public static String validateTag(String tag) {
        if (!StringUtils.hasText(tag)) {
            return "";
        }
        if (tag.length() > 10) {
            throw new InvalidRangeException(ErrorCode.INVALID_TAG_NAME_LENGTH);
        }
        if (!REGEX_WORKSPACE_TAG.matcher(tag).matches()) {
            throw new InvalidPatternException(ErrorCode.INVALID_TAG_NAME_FORMAT);
        }
        return tag;
    }

    public static String validateDescription(String description) {
        if (!StringUtils.hasText(description)) {
            return "";
        }
        return description;
    }

    public void validateDuplicateName(String workspaceName) {
        if (workspaceRepository.existsByName(workspaceName)) {
            throw new AlreadyExistException(ErrorCode.ALREADY_USED_WORKSPACE_NAME);
        }
    }

}
