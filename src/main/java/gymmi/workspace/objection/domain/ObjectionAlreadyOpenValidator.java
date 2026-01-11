package gymmi.workspace.objection.domain;

import gymmi.global.exceptionhandler.exception.AlreadyExistException;
import gymmi.global.exceptionhandler.message.ErrorCode;
import gymmi.workspace.objection.repository.ObjectionRepository;
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
