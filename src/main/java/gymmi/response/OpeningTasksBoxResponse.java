package gymmi.response;

import gymmi.entity.Task;
import gymmi.exception.ServerLogicFaultException;
import java.util.List;
import lombok.Getter;

@Getter
public class OpeningTasksBoxResponse {

    private final TaskDTO pickedTask;
    private final List<TaskDTO> tasks;

    public OpeningTasksBoxResponse(List<Task> tasks) {
        this.pickedTask = getPickedTask(tasks);
        this.tasks = getTask(tasks);
    }

    private List<TaskDTO> getTask(List<Task> tasks) {
        return tasks.stream()
                .map(t -> new TaskDTO(t.getId(), t.getName(), t.getRegister().getNickname()))
                .toList();
    }

    private TaskDTO getPickedTask(List<Task> tasks) {
        return tasks.stream()
                .filter(t -> t.isPicked())
                .findFirst()
                .map(t -> new TaskDTO(t.getId(), t.getName(), t.getRegister().getNickname()))
                .orElseThrow(() -> new ServerLogicFaultException("picked task is not exist: " + this.getClass()));
    }

}
