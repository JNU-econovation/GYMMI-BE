package gymmi.exceptionhandler;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.exceptionhandler.message.UserErrorMessage;

import java.util.HashMap;
import java.util.Map;

public class Mapper {

    private final Map<UserErrorMessage, ErrorCode> mapper = new HashMap<>();
    private final Map<ErrorCode, UserErrorMessage> a = new HashMap<>();


    public void init() {


    }
}
