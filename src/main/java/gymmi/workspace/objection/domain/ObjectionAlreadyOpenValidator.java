package gymmi.workspace.objection.domain;

import gymmi.global.exception.exceptiontype.AlreadyExistException;
import gymmi.global.exception.message.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectionAlreadyOpenValidator {

    public static void validate(boolean isPresent) {
        if (isPresent) {
            throw new AlreadyExistException(ErrorCode.ALREADY_OBJECTED);
        }
    }
}
