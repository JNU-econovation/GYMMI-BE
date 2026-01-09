package gymmi.etc.service;

import gymmi.etc.domain.DuplicationCheck;
import gymmi.global.exceptionhandler.exception.UnsupportedException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.etc.domain.DuplicationCheckType;
import gymmi.etc.controller.response.DuplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DuplicationCheckService {

    private final List<DuplicationCheck> duplicationCheckStrategies;

    public DuplicationResponse checkDuplication(DuplicationCheckType type, String value) {
        DuplicationCheck duplicationCheck = duplicationCheckStrategies.stream()
                .filter(s -> s.supports(type))
                .findAny()
                .orElseThrow(() -> new UnsupportedException(ErrorCode.UNSUPPORTED_TYPE));
        boolean isDuplicate = duplicationCheck.isDuplicate(value);
        return new DuplicationResponse(isDuplicate);
    }
}
