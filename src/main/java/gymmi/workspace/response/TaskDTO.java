package gymmi.workspace.response;

import lombok.Getter;

@Getter
public class TaskDTO {

    private final Long id;
    private final String task;
    private final String nickname;

    public TaskDTO(Long id, String task, String nickname) {
        this.id = id;
        this.task = task;
        this.nickname = nickname;
    }
}
