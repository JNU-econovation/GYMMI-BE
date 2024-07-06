package gymmi.service;

import gymmi.entity.Task;
import gymmi.entity.User;
import gymmi.entity.Worker;
import gymmi.exception.ServerLogicFaultException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class TaskDraw {

    // 기여퍼센트 + 2 * 등수(reversed)
    private final List<Task> tasks;

    public TaskDraw(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    private Task findBy(User user) {
        return tasks.stream()
                .filter(t -> t.isRegisteredBy(user))
                .findFirst()
                .orElseThrow(() -> new ServerLogicFaultException("task is not exist: " + this.getClass()));
    }

    private Task findBy(Long id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ServerLogicFaultException("task is not exist: " + this.getClass()));
    }

    public Task pickOneAmong(List<Worker> workers, List<Integer> ranks) {
        List<Long> box = new ArrayList<>();
        for (int i = 0; i < workers.size(); i++) {
            int score = (int) Math.round(workers.get(i).getContributedPercent()) + 2 * (ranks.size() - ranks.get(i));
            for (int j = 0; j < score; j++) {
                box.add(findBy(workers.get(i).getUser()).getId());
            }
        }
        SecureRandom random = new SecureRandom();
        Long taskId = box.get(random.nextInt(0, box.size()));
        return findBy(taskId);
    }
}
