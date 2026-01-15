package gymmi.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsBy(String nickname) {
        return false;
    }

    //
//    @Override
//    public boolean existsBy(String nickname) {
//        User user = jpaQueryFactory.select(QUser.user)
//                .from(QUser.user)
//                .where(QUser.user.nickname.eq(nickname))
//                .fetchFirst();
//        return user != null;
//    }
}
