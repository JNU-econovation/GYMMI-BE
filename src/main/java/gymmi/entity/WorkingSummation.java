package gymmi.entity;

import java.util.ArrayList;
import java.util.List;

public class WorkingSummation {

    private final List<WorkingRecord> workingRecords;

    public WorkingSummation(List<WorkingRecord> workingRecords) {
        this.workingRecords = new ArrayList<>(workingRecords);
    }

    public int getSumOfMissionScoreContributed(Mission mission) {
        return workingRecords.stream()
                .filter(wr -> wr.hasMission(mission))
                .map(WorkingRecord::getContributedScore)
                .reduce(0, Integer::sum);
    }

    public int getSumOfMissionCountWorked(Mission mission) {
        return workingRecords.stream()
                .filter(wr -> wr.hasMission(mission))
                .map(WorkingRecord::getWorkingCount)
                .reduce(0, Integer::sum);
    }

}
