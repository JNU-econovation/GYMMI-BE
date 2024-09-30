package gymmi.service;

import gymmi.exception.class1.UnsupportedException;
import gymmi.exception.message.ErrorCode;
import gymmi.global.DuplicationCheckType;
import gymmi.response.DuplicationResponse;
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
