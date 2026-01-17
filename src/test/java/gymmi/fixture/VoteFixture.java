package gymmi.fixture;

import gymmi.workspace.objection.domain.entity.Objection;
import gymmi.workspace.vote.domain.entity.Vote;
import gymmi.workspace.workspace.domain.entity.Worker;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class VoteFixture {

    public static VoteBuilder builder(Worker worker, Objection objection) {
        return new VoteBuilder(worker, objection);
    }

    public static class VoteBuilder {
        private final Worker worker;
        private final Objection objection;

        private Long id = 0L;
        private Boolean isApproved = true;
        private Boolean automatic = false;

        private VoteBuilder(Worker worker, Objection objection) {
            this.worker = worker;
            this.objection = objection;
        }

        public VoteBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public VoteBuilder isApproved(Boolean isApproved) {
            this.isApproved = isApproved;
            return this;
        }

        public Vote build() {
            Vote vote = new Vote(worker, objection, isApproved, automatic);

            if (id != null) {
                ReflectionTestUtils.setField(vote, "id", id);
            }

            return vote;
        }
    }
}
