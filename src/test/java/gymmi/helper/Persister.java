package gymmi.helper;

import gymmi.entity.User;
import gymmi.repository.UserRepository;
import gymmi.workspace.domain.*;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import jakarta.persistence.EntityManager;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.instancio.Select.field;

@Component
public class Persister {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    WorkerRepository workerRepository;

    public User persistUser() {
        User user = Instancio.of(User.class)
                .set(field(User::isResigned), false)
                .ignore(field(User::getId))
                .create();
        userRepository.save(user);
        return user;
    }

    public List<User> persistUsers(int size){
        List<User> users = Instancio.ofList(User.class)
                .size(size)
                .set(field(User::isResigned), false)
                .ignore(field(User::getId))
                .create();
        userRepository.saveAll(users);
        return users;
    }

    public List<Mission> persistMissions(Workspace workspace, int size) {
        List<Mission> missions = Instancio.ofList(Mission.class)
                .size(size)
                .set(Select.field(Mission::getWorkspace), workspace)
                .generate(Select.field(Mission::getScore),
                        gen -> gen.ints().range(Mission.MIN_SCORE, Mission.MAX_SCORE))
                .ignore(Select.field(Mission::getId))
                .create();
        for (Mission mission : missions) {
            entityManager.persist(mission);
        }
        return missions;
    }

    public Workspace persistWorkspace(User creator, int goalScore, int headCount) {
        Workspace workspace = Instancio.of(Workspace.class)
                .set(field(Workspace::getStatus), WorkspaceStatus.PREPARING)
                .set(field(Workspace::getGoalScore), goalScore)
                .set(field(Workspace::getHeadCount), headCount)
                .set(field(Workspace::getCreator), creator)
                .ignore(field(Workspace::getId))
                .create();
        workspaceRepository.save(workspace);
        return workspace;
    }

    public Worker persistWorker(User user, Workspace workspace) {
        Worker worker = new Worker(user, workspace, new Task(Instancio.gen().string().get()));
        workerRepository.save(worker);
        return worker;
    }


}
