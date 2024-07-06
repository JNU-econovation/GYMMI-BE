package gymmi.exception;

public class ServerLogicFaultException extends GymmiException {

    public static final String ERROR_CODE = "SERVER_LOGIC_FAULT";
    public static final String ERROR_DESCRIPTION = "서버 내 로직에서 결함이 생긴 경우";

    public ServerLogicFaultException(String message) {
        super(message, ERROR_CODE, ERROR_DESCRIPTION);
    }
}
