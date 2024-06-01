package gymmi.service;

import gymmi.exception.PreparingException;
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
                .orElseThrow(() -> new PreparingException("준비중인 type 입니다."));
        boolean isDuplicate = duplicationCheck.isDuplicate(value);
        return new DuplicationResponse(isDuplicate);
    }
}
