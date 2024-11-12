package gymmi.workspace.domain;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Objection;
import gymmi.workspace.domain.entity.Vote;
import gymmi.workspace.domain.entity.Worker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectionManagerTest {

    @Test
    void 이미_투표에_참여한_경우_예외가_발생한다() {
        // given
        Objection objection = getTackle(true);
        Vote vote = getVote(objection);
        addVoteToTackle(objection, vote);

        Worker worker = vote.getWorker();
        ObjectionManager tackleManager = new ObjectionManager(objection);

        // when, then
        assertThatThrownBy(() -> tackleManager.createVote(worker, true))
                .hasMessage(ErrorCode.ALREADY_VOTED.getMessage());
    }

    @Test
    void 종료된_투표에_투표하는_경우_예외가_발생한다() {
        // given
        Objection objection = getTackle(false);
        Vote vote = getVote(objection);
        addVoteToTackle(objection, vote);

        Worker worker = vote.getWorker();
        ObjectionManager tackleManager = new ObjectionManager(objection);

        // when, then
        assertThatThrownBy(() -> tackleManager.createVote(worker, true))
                .hasMessage(ErrorCode.ALREADY_CLOSED_OBJECTION.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "4,3,1,true",
            "4,2,1,false",
            "5,2,2,false",
            "4,2,2,true",
    })
    void 투표에_따라_투표가_종료된다(int headcount, int agreeCount, int disagreeCount, boolean isClose) {
        // given
        Objection objection = getTackle(true);
        int headCount = headcount;
        List<Vote> agreeVotes = getVotes(agreeCount, objection, true);
        List<Vote> disagreeVotes = getVotes(disagreeCount, objection, false);
        agreeVotes.addAll(disagreeVotes);
        addVoteToTackle(objection, agreeVotes);

        ObjectionManager tackleManager = new ObjectionManager(objection);

        // when
        tackleManager.closeIfOnMajorityOrDone(headCount);

        // then
        assertThat(!tackleManager.getObjection().isInProgress()).isEqualTo(isClose);
    }

    private List<Vote> getVotes(int size, Objection objection, boolean isAgree) {
        List<Vote> agreeVotes = Instancio.ofList(Vote.class)
                .size(size)
                .set(Select.field(Vote::getObjection), objection)
                .set(Select.field(Vote::getIsApproved), isAgree)
                .create();
        return agreeVotes;
    }

    private Vote getVote(Objection objection) {
        return Instancio.of(Vote.class)
                .set(Select.field(Vote::getObjection), objection)
                .create();
    }

    private void addVoteToTackle(Objection objection, Vote... vote) {
        ReflectionTestUtils.setField(objection, "votes", List.of(vote));
    }

    private void addVoteToTackle(Objection objection, List<Vote> votes) {
        ReflectionTestUtils.setField(objection, "votes", new ArrayList<>(votes));
    }

    private Objection getTackle(boolean isOpen) {
        return Instancio.of(Objection.class)
                .set(Select.field(Objection::isInProgress), isOpen)
                .create();
    }
}
