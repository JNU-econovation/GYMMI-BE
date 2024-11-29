package gymmi.helper;

import gymmi.entity.User;
import gymmi.photoboard.domain.entity.PhotoFeed;
import gymmi.photoboard.domain.entity.PhotoFeedImage;
import gymmi.repository.UserRepository;
import gymmi.workspace.domain.WorkspaceStatus;
import gymmi.workspace.domain.entity.*;
import gymmi.workspace.repository.WorkerRepository;
import gymmi.workspace.repository.WorkspaceRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.instancio.Select.field;

@Component
@Transactional
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

    public List<User> persistUsers(int size) {
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

    public Mission persistMission(Workspace workspace, int score) {
        Mission mission = Instancio.of(Mission.class)
                .set(field(Mission::getWorkspace), workspace)
                .set(field(Mission::getScore), score)
                .ignore(field(Mission::getId))
                .create();
        entityManager.persist(mission);
        return mission;
    }

    public FavoriteMission persistFavoriteMission(Worker worker, Mission mission) {
        FavoriteMission favoriteMission = new FavoriteMission(worker, mission);
        entityManager.persist(favoriteMission);
        return favoriteMission;
    }

    public Workspace persistWorkspace(User creator, WorkspaceStatus workspaceStatus, int goalScore, int headCount) {
        Workspace workspace = Instancio.of(Workspace.class)
                .set(field(Workspace::getStatus), workspaceStatus)
                .set(field(Workspace::getGoalScore), goalScore)
                .set(field(Workspace::getHeadCount), headCount)
                .set(field(Workspace::getCreator), creator)
                .ignore(field(Workspace::getId))
                .create();
        workspaceRepository.save(workspace);
        return workspace;
    }

    public Workspace persistWorkspace(User creator) {
        Workspace workspace = Instancio.of(Workspace.class)
                .generate(field(Workspace::getStatus), gen -> gen.enumOf(WorkspaceStatus.class))
                .set(field(Workspace::getGoalScore), Workspace.MIN_GOAL_SCORE)
                .set(field(Workspace::getHeadCount), Workspace.MIN_HEAD_COUNT)
                .set(field(Workspace::getCreator), creator)
                .ignore(field(Workspace::getId))
                .create();
        workspaceRepository.save(workspace);
        return workspace;
    }


    public Workspace persistWorkspace(User creator, WorkspaceStatus workspaceStatus) {
        Workspace workspace = Instancio.of(Workspace.class)
                .set(field(Workspace::getStatus), workspaceStatus)
                .set(field(Workspace::getGoalScore), Workspace.MIN_GOAL_SCORE)
                .set(field(Workspace::getHeadCount), Workspace.MIN_HEAD_COUNT)
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

    //todo
    public WorkoutHistory persistWorkoutHistoryAndApply(Worker worker, Map<Mission, Integer> workouts) {
        List<WorkoutRecord> workoutRecords = workouts.entrySet().stream()
                .map(workout -> new WorkoutRecord(workout.getKey(), workout.getValue()))
                .toList();
        Worker managedWorker = entityManager.find(Worker.class, worker.getId());
        WorkoutConfirmation workoutConfirmation = Instancio.of(WorkoutConfirmation.class)
                .ignore(Select.field(WorkoutConfirmation::getId))
                .create();
        entityManager.persist(workoutConfirmation);
        WorkoutHistory workoutHistory = new WorkoutHistory(
                managedWorker, workoutRecords, workoutConfirmation
        );
        entityManager.persist(workoutHistory);
        workoutHistory.apply();
        return workoutHistory;
    }

    public WorkoutHistory persistWorkoutHistoryAndApply(Worker worker, Map<Mission, Integer> workouts, WorkoutConfirmation workoutConfirmation) {
        List<WorkoutRecord> workoutRecords = workouts.entrySet().stream()
                .map(workout -> new WorkoutRecord(workout.getKey(), workout.getValue()))
                .toList();
        Worker managedWorker = entityManager.find(Worker.class, worker.getId());
        WorkoutHistory workoutHistory = new WorkoutHistory(
                managedWorker, workoutRecords, workoutConfirmation
        );
        entityManager.persist(workoutHistory);
        workoutHistory.apply();
        return workoutHistory;
    }

    public Objection persistObjection(Worker subject, boolean isInProgress, WorkoutConfirmation workoutConfirmation) {
        Objection objection = Instancio.of(Objection.class)
                .set(field(Objection::getSubject), subject)
                .set(field(Objection::isInProgress), isInProgress)
                .set(field(Objection::getWorkoutConfirmation), workoutConfirmation)
                .set(field(Objection::getVotes), new ArrayList<>())
                .ignore(field(Objection::getId))
                .create();
        entityManager.persist(objection);
        return objection;
    }

    public WorkoutConfirmation persistWorkoutConfirmation() {
        WorkoutConfirmation workoutConfirmation = Instancio.of(WorkoutConfirmation.class)
                .ignore(field(WorkoutConfirmation::getId))
                .create();
        entityManager.persist(workoutConfirmation);
        return workoutConfirmation;
    }

    public Vote persistVote(Worker worker, Objection objection, boolean isApproved) {
        Vote vote = Instancio.of(Vote.class)
                .set(field(Vote::getWorker), worker)
                .set(field(Vote::getObjection), objection)
                .set(field(Vote::getIsApproved), isApproved)
                .ignore(field(Vote::getId))
                .create();
        objection.add(vote);
        entityManager.persist(vote);
        return vote;
    }

    public PhotoFeed persistPhotoFeed(User user) {
        PhotoFeed photoFeed = Instancio.of(PhotoFeed.class)
                .set(field(PhotoFeed::getUser), user)
                .ignore(field(PhotoFeed::getId))
                .create();
        entityManager.persist(photoFeed);
        return photoFeed;
    }

    public PhotoFeed persistPhotoFeed(User user, int thumpsUpCount) {
        PhotoFeed photoFeed = Instancio.of(PhotoFeed.class)
                .set(field(PhotoFeed::getUser), user)
                .set(field(PhotoFeed::getThumpsUpCount), thumpsUpCount)
                .ignore(field(PhotoFeed::getId))
                .create();
        entityManager.persist(photoFeed);
        return photoFeed;
    }

    public PhotoFeed persistPhotoFeed(User user, boolean isModified) {
        PhotoFeed photoFeed = Instancio.of(PhotoFeed.class)
                .set(field(PhotoFeed::getUser), user)
                .set(field(PhotoFeed::isModified), isModified)
                .ignore(field(PhotoFeed::getId))
                .create();
        entityManager.persist(photoFeed);
        return photoFeed;
    }

    public PhotoFeedImage persistPhotoFeedImage(PhotoFeed photoFeed) {
        PhotoFeedImage photoFeedImage = Instancio.of(PhotoFeedImage.class)
                .set(field(PhotoFeedImage::getPhotoFeed), photoFeed)
                .ignore(field(PhotoFeedImage::getId))
                .create();
        entityManager.persist(photoFeedImage);
        return photoFeedImage;
    }
}
