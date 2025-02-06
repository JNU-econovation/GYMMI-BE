package gymmi.workspace.domain;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.domain.entity.Worker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectionManagerTest {

    @Nested
    class 투표_참여 {

        @Test
        void 이미_투표에_참여한_경우_예외가_발생한다() {
            // given
            Objection objection = getObjection(true);
            Vote vote = getVote(objection);
            addVoteToObjection(objection, vote);

            Worker worker = vote.getWorker();
            ObjectionManager tackleManager = new ObjectionManager(objection);

            // when, then
            assertThatThrownBy(() -> tackleManager.createVote(worker, true))
                    .hasMessage(ErrorCode.ALREADY_VOTED.getMessage());
        }

        @Test
        void 종료된_투표에_투표하는_경우_예외가_발생한다() {
            // given
            Objection objection = getObjection(false);
            Vote vote = getVote(objection);
            addVoteToObjection(objection, vote);

            Worker worker = vote.getWorker();
            ObjectionManager tackleManager = new ObjectionManager(objection);

            // when, then
            assertThatThrownBy(() -> tackleManager.createVote(worker, true))
                    .hasMessage(ErrorCode.ALREADY_CLOSED_OBJECTION.getMessage());
        }

    }

    @ParameterizedTest
    @CsvSource(value = {"23,false", "24, true"})
    void 마감기간이_지남_여부를_확인한다(int hours, boolean expired) {
        // given
        Objection objection = getObjection(true, LocalDateTime.now().minusHours(hours));

        // when
        boolean result = objection.isExpired();

        // then
        assertThat(result).isEqualTo(expired);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4,3,1,true, true",
            "4,2,1,false, false",
            "5,2,2,false, false",
            "4,2,2,true, false",
    })
    void 투표에_따라_이의신청이_종료된다(int workerCount, int agreeCount, int disagreeCount, boolean isClosed, boolean isApproved) {
        // given
        Objection objection = getObjection(true);
        List<Vote> agreeVotes = getVotes(agreeCount, objection, true);
        List<Vote> disagreeVotes = getVotes(disagreeCount, objection, false);
        agreeVotes.addAll(disagreeVotes);
        addVoteToObjection(objection, agreeVotes);

        ObjectionManager objectionManager = new ObjectionManager(objection);

        // when
        boolean result = objectionManager.closeIfOnMajorityOrDone(workerCount);

        // then
        assertThat(!objectionManager.getObjection().isInProgress()).isEqualTo(isClosed);
        assertThat(result).isEqualTo(isClosed);
        assertThat(objectionManager.isApproved()).isEqualTo(isApproved);
    }

    private List<Vote> getVotes(int size, Objection objection, boolean isApproved) {
        List<Vote> agreeVotes = Instancio.ofList(Vote.class)
                .size(size)
                .set(Select.field(Vote::getObjection), objection)
                .set(Select.field(Vote::getIsApproved), isApproved)
                .create();
        return agreeVotes;
    }

    private Vote getVote(Objection objection) {
        return Instancio.of(Vote.class)
                .set(Select.field(Vote::getObjection), objection)
                .create();
    }

    private void addVoteToObjection(Objection objection, Vote... vote) {
        ReflectionTestUtils.setField(objection, "votes", List.of(vote));
    }

    private void addVoteToObjection(Objection objection, List<Vote> votes) {
        ReflectionTestUtils.setField(objection, "votes", new ArrayList<>(votes));
    }

    private Objection getObjection(boolean isInProgress) {
        return Instancio.of(Objection.class)
                .set(Select.field(Objection::isInProgress), isInProgress)
                .create();
    }

    private Objection getObjection(boolean isOpen, LocalDateTime createdAt) {
        return Instancio.of(Objection.class)
                .set(Select.field(Objection::isInProgress), isOpen)
                .set(Select.field(Objection::getCreatedAt), createdAt)
                .create();
    }

}
