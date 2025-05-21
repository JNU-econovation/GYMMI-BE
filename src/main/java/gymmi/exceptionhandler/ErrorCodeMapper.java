package gymmi.exceptionhandler;

import gymmi.exceptionhandler.message.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ErrorCodeMapper {

    private static final Map<ErrorCode, Set<ErrorMessage>> mapper = new HashMap<>();
    private static final Map<ErrorMessage, ErrorCode> errorCodeFromErrorMessage = new HashMap<>();

    static {
        mapper.put(ErrorCode.AUTH_FAIL,
                Stream.of(LoginErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.REGISTER_FAIL,
                Stream.of(RegisterErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.WORKSPACE_START_FAIL,
                Stream.of(WorkspaceStartErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.WORKSPACE_JOIN_FAIL,
                Stream.of(WorkspaceJoinErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.WORKSPACE_EDIT_FAIL,
                Stream.of(WorkspaceEditErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.WORKSPACE_CREATION_FAIL,
                Stream.of(WorkspaceCreationErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.WORKOUT_FAIL,
                Stream.of(WorkoutErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.VOTE_FAIL,
                Stream.of(VoteErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.OBJECTION_FAIL,
                Stream.of(ObjectionErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.PHOTO_FEED_DELETION_FAIL,
                Stream.of(PhotoFeedDeletionErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.NOT_FOUND,
                Stream.of(NotFoundErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.INVALID_FILE,
                Stream.of(FileErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.INVALID_QUERY_PARM,
                Stream.of(InvalidQueryParmErrorMessage.values()).collect(Collectors.toSet()));

        putServerError();

        mapper.put(ErrorCode.INVALID_ACCESS,
                Stream.of(InvalidAccessErrorMessage.values()).collect(Collectors.toSet()));

        mapper.put(ErrorCode.WORKSPACE_DRAW_FAIL, Set.of(
                WorkspaceErrorMessage.NOT_COMPLETED_WORKSPACE,
                WorkspaceErrorMessage.EXIST_OBJECTION_IN_PROGRESS
        ));

        mapper.put(ErrorCode.WORKSPACE_EXIT_FAIL, Set.of(
                WorkspaceErrorMessage.EXIST_WORKERS_EXCLUDE_CREATOR
        ));

        initReverseMapper();
    }

    private static void initReverseMapper() {
        for (Map.Entry<ErrorCode, Set<ErrorMessage>> errorCodeSetEntry : mapper.entrySet()) {
            for (ErrorMessage errorMessage : errorCodeSetEntry.getValue()) {
                errorCodeFromErrorMessage.put(errorMessage, errorCodeSetEntry.getKey());
            }
        }
    }

    private ErrorCodeMapper() {
    }

    private static void putServerError() {
        mapper.put(ErrorCode.SERVER_ERROR,
                Stream.of(ServerErrorMessage.values()).collect(Collectors.toSet())
        );
        mapper.put(ErrorCode.SERVER_ERROR,
                Stream.of(WorkspaceConsistencyErrorMessage.values()).collect(Collectors.toSet())
        );
    }

    public static ErrorCode getErrorCodeFrom(ErrorMessage errorMessage) {
        return errorCodeFromErrorMessage.get(errorMessage);
    }

    public static void printMapper() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            Set<ErrorMessage> value = mapper.get(errorCode);
            System.out.println("==[에러코드]==");
            System.out.println("에러코드: " + errorCode.name());
            System.out.println("설명: " + errorCode.getValue());
            System.out.println("상태코드: " + errorCode.getStatusCode());
            System.out.println("관련된 예외 메시지");
            if (value != null) {
                for (ErrorMessage errorMessage : value) {
                    System.out.print("\t");
                    System.out.println(errorMessage.getMessage());
                }
            }
        }
    }

}
