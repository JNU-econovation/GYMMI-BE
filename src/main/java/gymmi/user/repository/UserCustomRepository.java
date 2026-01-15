package gymmi.user.repository;

public interface UserCustomRepository {

    boolean existsBy(String nickname);

}
