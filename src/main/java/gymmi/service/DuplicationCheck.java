package gymmi.service;

import gymmi.global.DuplicationCheckType;

public interface DuplicationCheck {

    boolean supports(DuplicationCheckType type);

    boolean isDuplicate(String value);
}
