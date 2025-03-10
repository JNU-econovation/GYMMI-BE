//package gymmi.integration;
//
//import gymmi.exceptionhandler.exception.NotFoundException;
//import gymmi.workspace.request.*;
//import gymmi.workspace.response.MissionResponse;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpHeaders;
//
//import java.util.List;
//
//import static gymmi.Fixtures.*;
//import static gymmi.integration.Steps.*;
//
//@Disabled
//public class WorkspaceEndToEndTest extends EndToEndTest {
//
//    String defaultUserToken;
//    String user1Token;
//
//    @BeforeEach
//    void 인증_인가() {
//        RegistrationRequest request = 회원_가입__DEFAULT_USER_REQUEST;
//        this.defaultUserToken = 회원가입_및_로그인_요청(request);
//        RegistrationRequest request1 = 회원_가입__USER_1_REQUEST;
//        this.user1Token = 회원가입_및_로그인_요청(request1);
//    }
//
//    @Nested
//    class 워크스페이스_생성 {
//
//        @Test
//        void 워크스페이스를_생성을_성공한다_200() {
//            // given
//            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .name(WORKSPACE__SATISFIED_NAME)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(
//                            List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                                    MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//
//            // when
//            Response response = 워크스페이스_생성_요청(defaultUserToken, request);
//
//            // then
//            response.then()
//                    .statusCode(200)
//                    .body(JSON_KEY_ID, Matchers.notNullValue());
//        }
//
//        @Test
//        void 워크스페이스_이름이_이미_존재하는_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .name(WORKSPACE__SATISFIED_NAME)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(
//                            List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                                    MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//
//            CreatingWorkspaceRequest request = CreatingWorkspaceRequest.builder()
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .name(WORKSPACE__SATISFIED_NAME)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(
//                            List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                                    MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//            워크스페이스_생성_요청(defaultUserToken, step);
//
//            // when
//            Response response = 워크스페이스_생성_요청(defaultUserToken, request);
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//
//        @Test
//        void 참여_가능한_수가_초과하는_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
//                    .name("지미")
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                            MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//            워크스페이스_생성_요청(defaultUserToken, step);
//
//            CreatingWorkspaceRequest step1 = CreatingWorkspaceRequest.builder()
//                    .name("지미1")
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                            MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//            워크스페이스_생성_요청(defaultUserToken, step1);
//
//            CreatingWorkspaceRequest step2 = CreatingWorkspaceRequest.builder()
//                    .name("지미2")
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                            MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//            워크스페이스_생성_요청(defaultUserToken, step2);
//
//            CreatingWorkspaceRequest step3 = CreatingWorkspaceRequest.builder()
//                    .name("지미3")
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                            MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//            워크스페이스_생성_요청(defaultUserToken, step3);
//
//            CreatingWorkspaceRequest step4 = CreatingWorkspaceRequest.builder()
//                    .name("지미4")
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                            MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//            워크스페이스_생성_요청(defaultUserToken, step4);
//
//            CreatingWorkspaceRequest step5 = CreatingWorkspaceRequest.builder()
//                    .name("지미5")
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                            MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//
//            // when
//            Response response = 워크스페이스_생성_요청(defaultUserToken, step5);
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//    }
//
//    @Nested
//    class 워크스페이스_소개_보기 {
//
//        @Test
//        void 워크스페이스의_소개_확인한다() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            // when
//            Response response = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId);
//
//            // then
//            response.then()
//                    .statusCode(200);
//        }
//
//        @Test
//        void 워크스페이스_참여자가_아닌_경우_실패한다_403() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            // when
//            Response response = 워크스페이스_소개_보기_요청(user1Token, workspaceId);
//
//            // then
//            response.then()
//                    .statusCode(403);
//        }
//    }
//
//    @Nested
//    class 워크스페이스_참여 {
//
//        @Test
//        void 워크스페이스_참여에_성공한다_200() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//
//            JoiningWorkspaceRequest request = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//
//            // when
//            Response response = 워크스페이스_참여_요청(user1Token, workspaceId, request);
//
//            // then
//            response.then()
//                    .statusCode(200);
//        }
//
//        @Test
//        void 워크스페이스에_이미_참여한_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//
//            JoiningWorkspaceRequest request = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//
//            워크스페이스_참여_요청(user1Token, workspaceId, request);
//
//            // when
//            Response response = 워크스페이스_참여_요청(user1Token, workspaceId, request);
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//
//        @Test
//        void 워크스페이스_인원이_꽉_찬_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(2)
//                    .name(WORKSPACE__SATISFIED_NAME)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(
//                            List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                                    MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//
//            JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//            워크스페이스_참여_요청(user1Token, workspaceId, step1);
//
//            RegistrationRequest step2 = 회원_가입__USER_2_REQUEST;
//            String user2Token = 회원가입_및_로그인_요청(step2);
//
//            // when
//            Response response = 워크스페이스_참여_요청(user2Token, workspaceId, step1);
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//
//        @Test
//        void 워크스페이스가_준비중이_아닌_경우_실패한다_400() {
//            CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(3)
//                    .name(WORKSPACE__SATISFIED_NAME)
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(
//                            List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                                    MISSION__SATISFIED_MISSION_SCORE)))
//                    .build();
//
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//
//            JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//            워크스페이스_참여_요청(user1Token, workspaceId, step1);
//            워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//            RegistrationRequest step3 = 회원_가입__USER_2_REQUEST;
//            String user2Token = 회원가입_및_로그인_요청(step3);
//
//            // when
//            Response response = 워크스페이스_참여_요청(user2Token, workspaceId, step1);
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//
//        @Test
//        void 비밀번호가_일치하지_않는_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//            long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            JoiningWorkspaceRequest request = new JoiningWorkspaceRequest(WORKSPACE__DISSATISFIED_PASSWORD,
//                    TASK__DEFAULT_TASK);
//
//            // when
//            Response response = 워크스페이스_참여_요청(user1Token, workspaceId, request);
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//
//        @Test
//        void 참여가능한_수를_초과한_경우_실패한다_400() {
//            // given
//
//            // when
//
//            // then
//
//        }
//    }
//
//
//    @Test
//    void 워크스페이스의_비밀번호_일치_여부_확인을_성공한다_200() {
//        // given
//        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//        long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                .jsonPath()
//                .getLong(JSON_KEY_ID);
//
//        MatchingWorkspacePasswordRequest request = new MatchingWorkspacePasswordRequest(
//                WORKSPACE__DISSATISFIED_PASSWORD);
//
//        // when
//        Response response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
//                .pathParam("workspaceId", workspaceId)
//                .body(request)
//                .when().post("/workspaces/{workspaceId}/match-password");
//
//        // then
//        response.then()
//                .statusCode(200)
//                .body("sameness", Matchers.equalTo(false));
//    }
//
//    @Test
//    void 참여한_워크스페이스_목록_확인을_성공한다_200() {
//        // given
//        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//        워크스페이스_생성_요청(defaultUserToken, step);
//
//        // when
//        Response response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
//                .queryParam("page", 0)
//                .when().get("/workspaces/my");
//
//        // then
//        response.then().log().all()
//                .body("[0]", Matchers.notNullValue());
//    }
//
//    @Test
//    void 모든_워크스페이스_목록_확인을_성공한다_200() {
//        // given
//        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//        워크스페이스_생성_요청(defaultUserToken, step);
//
//        // when
//        Response response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
//                .queryParam("page", 0)
//                .when().get("/workspaces");
//
//        // then
//        response.then().log().all()
//                .body("[0]", Matchers.notNullValue());
//    }
//
//    @Nested
//    class 워크스페이스_시작 {
//        @Test
//        void 워크스페이스_시작을_성공한다_200() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//
//            워크스페이스_참여_요청(user1Token, workspaceId, new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK));
//
//            // when
//            Response response = 워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//            // then
//            response.then()
//                    .statusCode(200);
//        }
//
//        @Test
//        void 방장이_아닌_경우_실패한다_403() {
//            //given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//
//            워크스페이스_참여_요청(user1Token, workspaceId, new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK));
//
//            // when
//            Response response = 워크스페이스_시작_요청(user1Token, workspaceId);
//
//            // then
//            response.then()
//                    .statusCode(403);
//        }
//
//        @Test
//        void 참여자가_1명인_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            // when
//            Response response = 워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//
//
//        @Nested
//        class 워크스페이스_나가기 {
//            @Test
//            void 워크스페이스_나가기를_성공한다_200() {
//                // given
//                CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//                Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                        .jsonPath()
//                        .getLong(JSON_KEY_ID);
//
//                // when
//                Response response = 워크스페이스_나가기_요청(defaultUserToken, workspaceId);
//
//                // then
//                response.then()
//                        .statusCode(200);
//            }
//
//
//            @Test
//            void 방장일때_참여자가_존재하는_경우_실패한다_400() {
//                // given
//                CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//                Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                        .jsonPath()
//                        .getLong(JSON_KEY_ID);
//
//                String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                        .jsonPath()
//                        .getString(JSON_KEY_PASSWORD);
//
//                JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//                워크스페이스_참여_요청(user1Token, workspaceId, step1);
//
//                // when
//                Response response = 워크스페이스_나가기_요청(defaultUserToken, workspaceId);
//
//                // then
//                response.then()
//                        .statusCode(400);
//            }
//
//            @Test
//            void 준비중이_아닌경우_실패한다_400() {
//                // given
//                CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//                Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                        .jsonPath()
//                        .getLong(JSON_KEY_ID);
//
//                String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                        .jsonPath()
//                        .getString(JSON_KEY_PASSWORD);
//
//                JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//                워크스페이스_참여_요청(user1Token, workspaceId, step1);
//                워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//                // when
//                Response response = 워크스페이스_나가기_요청(defaultUserToken, workspaceId);
//
//                // then
//                response.then()
//                        .statusCode(400);
//            }
//        }
//    }
//
//    @Test
//    void 워크스페이스_입장에_성공한다_200() {
//        // given
//        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//        Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                .jsonPath()
//                .getLong(JSON_KEY_ID);
//
//        String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                .jsonPath()
//                .getString(JSON_KEY_PASSWORD);
//
//        JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//        워크스페이스_참여_요청(user1Token, workspaceId, step1);
//
//        // when
//        Response response = 워크스페이스_입장_요청(user1Token, workspaceId);
//
//        // then
//        response.then()
//                .statusCode(200)
//                .body("workers[0].isCreator", Matchers.equalTo(false))
//                .body("workers[1].isCreator", Matchers.equalTo(true))
//                .body("workers[0].isMyself", Matchers.equalTo(true))
//                .body("workers[1].isMyself", Matchers.equalTo(false));
//
//    }
//
//    @Test
//    void 워크스페이스에서_수행_가능한_미션목록_확인을_성공한다_200() {
//        // given
//        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//
//        Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                .jsonPath()
//                .getLong(JSON_KEY_ID);
//
//        // when
//        Response response = 워크스페이스_미션_보기_요청(defaultUserToken, workspaceId);
//
//        // then
//        response.then()
//                .statusCode(200)
//                .body("[0]", Matchers.notNullValue());
//    }
//
//    @Nested
//    class 미션_수행 {
//        @Test
//        void 다른_워크스페이스에_등록된_미션을_수행하는_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//            JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//            워크스페이스_참여_요청(user1Token, workspaceId, step1);
//            워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//            CreatingWorkspaceRequest step2 = CreatingWorkspaceRequest.builder()
//                    .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                    .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                    .name("지미지미")
//                    .task(TASK__DEFAULT_TASK)
//                    .missionBoard(
//                            List.of(new MissionRequest(MISSION__SATISFIED_MISSION_NAME,
//                                    MISSION__SATISFIED_MISSION_SCORE))
//                    ).build();
//            Long anotherWorkspaceId = 워크스페이스_생성_요청(defaultUserToken, step2)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//            Long anotherMissionId = 워크스페이스_미션_보기_요청(defaultUserToken, anotherWorkspaceId)
//                    .jsonPath()
//                    .getLong("[0].id");
//
//            // when
//            Response response = 미션_수행_요청(
//                    defaultUserToken,
//                    workspaceId,
//                    List.of(new WorkingMissionInWorkspaceRequest(anotherMissionId, 1))
//            );
//
//            // then
//            response.then()
//                    .body(JSON_KEY_ERROR_CODE, Matchers.equalTo(NotFoundException.EXCEPTION_CODE));
//        }
//
//        @Test
//        void 워크스페이스가_시작_중이_아닌_경우_실패한다_400() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            Long missionId = 워크스페이스_미션_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getLong("[0].id");
//
//            // when
//            Response response = 미션_수행_요청(
//                    defaultUserToken,
//                    workspaceId,
//                    List.of(new WorkingMissionInWorkspaceRequest(missionId, 1))
//            );
//
//            // then
//            response.then()
//                    .statusCode(400);
//        }
//
//
//        @Test
//        void 미션_수행을_성공한다_200() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//            String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//            JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//            워크스페이스_참여_요청(user1Token, workspaceId, step1);
//            워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//            List<MissionResponse> responses = 워크스페이스_미션_보기_요청(defaultUserToken, workspaceId)
//                    .as(new ParameterizedTypeReference<List<MissionResponse>>() {
//                    }.getType());
//            MissionResponse mission = responses.get(0);
//
//            // when
//            int count = 500;
//            Response response = 미션_수행_요청(
//                    defaultUserToken,
//                    workspaceId,
//                    List.of(new WorkingMissionInWorkspaceRequest(mission.getId(), count))
//            );
//            Response response1 = 미션_수행_요청(
//                    defaultUserToken,
//                    workspaceId,
//                    List.of(new WorkingMissionInWorkspaceRequest(mission.getId(), count))
//            );
//
//            // then
//            response.then()
//                    .statusCode(200)
//                    .body("workingScore", Matchers.equalTo((mission.getScore()) * count));
//            response1.then()
//                    .statusCode(400);
//        }
//    }
//
//    @Test
//    void 워크스페이스_참여자의_미션_합계_보기를_성공한다_200() {
//        // given
//        List<MissionRequest> missionRequests = List.of(
//                new MissionRequest("미션", 5),
//                new MissionRequest("미션1", 10)
//        );
//        CreatingWorkspaceRequest step = CreatingWorkspaceRequest.builder()
//                .goalScore(WORKSPACE__SATISFIED_GOAL_SCORE)
//                .headCount(WORKSPACE__SATISFIED_HEAD_COUNT)
//                .name(WORKSPACE__SATISFIED_NAME)
//                .task(TASK__DEFAULT_TASK)
//                .missionBoard(missionRequests)
//                .build();
//        Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                .jsonPath()
//                .getLong(JSON_KEY_ID);
//        String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                .jsonPath()
//                .getString(JSON_KEY_PASSWORD);
//        JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//        워크스페이스_참여_요청(user1Token, workspaceId, step1);
//        워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//        List<MissionResponse> responses = 워크스페이스_미션_보기_요청(defaultUserToken, workspaceId)
//                .as(new ParameterizedTypeReference<List<MissionResponse>>() {
//                }.getType());
//        MissionResponse mission = responses.get(0);
//        MissionResponse mission1 = responses.get(1);
//        미션_수행_요청(
//                defaultUserToken,
//                workspaceId,
//                List.of(
//                        new WorkingMissionInWorkspaceRequest(mission.getId(), 2),
//                        new WorkingMissionInWorkspaceRequest(mission1.getId(), 3)
//                )
//        );
//        미션_수행_요청(
//                defaultUserToken,
//                workspaceId,
//                List.of(
//                        new WorkingMissionInWorkspaceRequest(mission.getId(), 5)
//                )
//        );
//
//        // when
//        Response response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
//                .pathParam("workspaceId", workspaceId)
//                .pathParam("userId", 1L) // how can I get this specific id?
//                .when().get("/workspaces/{workspaceId}/workings/{userId}");
//        response.then().log().all();
//
//        // then
//        response.then()
//                .statusCode(200)
//                .body("[0].mission", Matchers.equalTo("미션"))
//                .body("[0].totalCount", Matchers.equalTo(7))
//                .body("[0].totalContributedScore", Matchers.equalTo(35))
//                .body("[1].mission", Matchers.equalTo("미션1"))
//                .body("[1].totalCount", Matchers.equalTo(3))
//                .body("[1].totalContributedScore", Matchers.equalTo(30));
//    }
//
//    @Test
//    void 당첨_테스크를_확인을_성공한다_200() {
//        // given
//        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//        Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                .jsonPath()
//                .getLong(JSON_KEY_ID);
//        String password = 워크스페이스_소개_보기_요청(defaultUserToken, workspaceId)
//                .jsonPath()
//                .getString(JSON_KEY_PASSWORD);
//        JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//        워크스페이스_참여_요청(user1Token, workspaceId, step1);
//        워크스페이스_시작_요청(defaultUserToken, workspaceId);
//
//        List<MissionResponse> responses = 워크스페이스_미션_보기_요청(defaultUserToken, workspaceId)
//                .as(new ParameterizedTypeReference<List<MissionResponse>>() {
//                }.getType());
//        MissionResponse mission = responses.get(0);
//        미션_수행_요청(
//                defaultUserToken,
//                workspaceId,
//                List.of(
//                        new WorkingMissionInWorkspaceRequest(mission.getId(), 100)
//                )
//        );
//
//        // when
//        Response response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
//                .pathParam("workspaceId", workspaceId)
//                .when().get("/workspaces/{workspaceId}/tasks");
//
//        // then
//        response.then().log().all()
//                .statusCode(200)
//                .body("pickedTask", Matchers.notNullValue())
//                .body("tasks", Matchers.hasSize(1));
//    }
//
//    @Nested
//    class 워크스페이스_소개_수정 {
//        @Test
//        void 워크스페이스_소개_수정을_성공한다_200() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//
//            // when
//            Response response =
//                    워크스페이스_설명_수정_요청(defaultUserToken, workspaceId,
//                            new EditingIntroductionOfWorkspaceRequest("수정", "태그", "테스크"));
//
//            // then
//            response.then()
//                    .statusCode(200);
//        }
//
//        @Test
//        void 방장이_아닌_경우_실패한다_403() {
//            // given
//            CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//            Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                    .jsonPath()
//                    .getLong(JSON_KEY_ID);
//            String password = 워크스페이스_소개_보기_요청(user1Token, workspaceId)
//                    .jsonPath()
//                    .getString(JSON_KEY_PASSWORD);
//            JoiningWorkspaceRequest step1 = new JoiningWorkspaceRequest(password, TASK__DEFAULT_TASK);
//            워크스페이스_참여_요청(user1Token, workspaceId, step1);
//
//            // when
//            Response response =
//                    워크스페이스_설명_수정_요청(user1Token, workspaceId, new EditingIntroductionOfWorkspaceRequest("수정", "태그", "테스크"));
//
//            // then
//            response.then()
//                    .statusCode(403);
//        }
//    }
//
//    @Test
//    void 워크스페이스_입장_가능_여부_확인을_성공한다_200() {
//        // given
//        CreatingWorkspaceRequest step = 워크스페이스_생성__DEFAULT_WORKSPACE_REQUEST;
//        Long workspaceId = 워크스페이스_생성_요청(defaultUserToken, step)
//                .jsonPath()
//                .getLong(JSON_KEY_ID);
//
//        // when
//        Response response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
//                .pathParam("workspaceId", workspaceId)
//                .when().get("/workspaces/{workspaceId}/enter");
//        response.then().log().all();
//
//        // then
//        response.then()
//                .statusCode(200)
//                .body("isWorker", Matchers.equalTo(true))
//                .body("isFull", Matchers.equalTo(false));
//    }
//
//    @Test
//    void 워크스페이스_생성_및_참여_가능_여부를_확인하면_성공한다_200() {
//        // given, when
//        Response response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TYPE_BEARER + defaultUserToken)
//                .when().get("/workspaces/check-creation");
//        response.then().log().all();
//
//        // then
//        response.then()
//                .statusCode(200)
//                .body("canCreate", Matchers.equalTo(true));
//    }
//}
//
