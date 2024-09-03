package gymmi.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gymmi.entity.QUser;
import gymmi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsBy(String nickname) {
        User user = jpaQueryFactory.select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.nickname.eq(nickname))
                .fetchFirst();
        return user != null;
    }
}
