package gymmi.global.check.domain;

public interface DuplicationCheck {

    boolean supports(DuplicationCheckType type);

    boolean isDuplicate(String value);
}
