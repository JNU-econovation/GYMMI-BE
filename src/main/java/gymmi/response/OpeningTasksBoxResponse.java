package gymmi.response;

import gymmi.entity.Task;
import gymmi.exception.ServerLogicFaultException;
import lombok.Getter;

import java.util.List;

@Getter
public class OpeningTasksBoxResponse {

    private final TaskDTO pickedTask;
    private final List<TaskDTO> tasks;

    public OpeningTasksBoxResponse(List<Task> tasks) {
        this.pickedTask = getPickedTask(tasks);
        this.tasks = getRestTasks(tasks);
    }

    private List<TaskDTO> getRestTasks(List<Task> tasks) {
        return tasks.stream()
                .filter(t -> !t.isPicked())
//                .map(t -> new TaskDTO(t.getId(), t.getName(), t.getRegister().getNickname()))
                .map(t -> new TaskDTO(t.getId(), t.getName(), null))
                .toList();
    }

    private TaskDTO getPickedTask(List<Task> tasks) {
        return tasks.stream()
                .filter(t -> t.isPicked())
                .findFirst()
//                .map(t -> new TaskDTO(t.getId(), t.getName(), t.getRegister().getNickname()))
                .map(t -> new TaskDTO(t.getId(), t.getName(), null))
                .orElseThrow(() -> new ServerLogicFaultException("picked task is not exist: " + this.getClass()));
    }

}
