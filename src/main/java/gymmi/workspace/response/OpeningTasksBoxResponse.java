package gymmi.workspace.response;

import gymmi.exceptionhandler.legacy.ServerLogicFaultException;
import gymmi.workspace.domain.entity.Task;
import java.util.List;
import lombok.Getter;

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
