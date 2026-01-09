package gymmi.etc.domain;

public interface DuplicationCheck {

    boolean supports(DuplicationCheckType type);

    boolean isDuplicate(String value);
}
