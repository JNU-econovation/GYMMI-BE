package gymmi.workspace.domain;

import gymmi.exceptionhandler.message.ErrorCode;
import gymmi.workspace.domain.entity.Tackle;
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

class TackleManagerTest {

    @Test
    void 이미_투표에_참여한_경우_예외가_발생한다() {
        // given
        Tackle tackle = getTackle(true);
        Vote vote = getVote(tackle);
        addVoteToTackle(tackle, vote);

        Worker worker = vote.getWorker();
        TackleManager tackleManager = new TackleManager(tackle);

        // when, then
        assertThatThrownBy(() -> tackleManager.createVote(worker, true))
                .hasMessage(ErrorCode.ALREADY_VOTED.getMessage());
    }

    @Test
    void 종료된_투표에_투표하는_경우_예외가_발생한다() {
        // given
        Tackle tackle = getTackle(false);
        Vote vote = getVote(tackle);
        addVoteToTackle(tackle, vote);

        Worker worker = vote.getWorker();
        TackleManager tackleManager = new TackleManager(tackle);

        // when, then
        assertThatThrownBy(() -> tackleManager.createVote(worker, true))
                .hasMessage(ErrorCode.ALREADY_CLOSED_TACKLE.getMessage());
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
        Tackle tackle = getTackle(true);
        int headCount = headcount;
        List<Vote> agreeVotes = getVotes(agreeCount, tackle, true);
        List<Vote> disagreeVotes = getVotes(disagreeCount, tackle, false);
        agreeVotes.addAll(disagreeVotes);
        addVoteToTackle(tackle, agreeVotes);

        TackleManager tackleManager = new TackleManager(tackle);

        // when
        tackleManager.closeIfOnMajorityOrDone(headCount);

        // then
        assertThat(!tackleManager.getTackle().isOpen()).isEqualTo(isClose);
    }

    private List<Vote> getVotes(int size, Tackle tackle, boolean isAgree) {
        List<Vote> agreeVotes = Instancio.ofList(Vote.class)
                .size(size)
                .set(Select.field(Vote::getTackle), tackle)
                .set(Select.field(Vote::getIsAgree), isAgree)
                .create();
        return agreeVotes;
    }

    private Vote getVote(Tackle tackle) {
        return Instancio.of(Vote.class)
                .set(Select.field(Vote::getTackle), tackle)
                .create();
    }

    private void addVoteToTackle(Tackle tackle, Vote... vote) {
        ReflectionTestUtils.setField(tackle, "votes", List.of(vote));
    }

    private void addVoteToTackle(Tackle tackle, List<Vote> votes) {
        ReflectionTestUtils.setField(tackle, "votes", new ArrayList<>(votes));
    }

    private Tackle getTackle(boolean isOpen) {
        return Instancio.of(Tackle.class)
                .set(Select.field(Tackle::isOpen), isOpen)
                .create();
    }
}
