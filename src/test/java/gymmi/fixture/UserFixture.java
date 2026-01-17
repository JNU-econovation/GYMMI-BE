package gymmi.fixture;

import gymmi.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

public abstract class UserFixture {


    public static User defaultUser() {
        return UserFixture.builder().build();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private Long id = 0L;
        private String loginId = "gymmi123";          // 영어+숫자 포함
        private String plainPassword = "password123!"; // 영어+숫자+특수문자 포함
        private String nickname = "지미닉네임";
        private String email = "test@gymmi.com";
        private boolean isResigned = false;

        private UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder loginId(String loginId) {
            this.loginId = loginId;
            return this;
        }

        public UserBuilder plainPassword(String plainPassword) {
            this.plainPassword = plainPassword;
            return this;
        }

        public UserBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder isResigned(boolean isResigned) {
            this.isResigned = isResigned;
            return this;
        }


        // 2. 최종 객체 생성 및 리플렉션 주입
        public User build() {
            User user = User.builder()
                    .loginId(loginId)
                    .plainPassword(plainPassword)
                    .nickname(nickname)
                    .email(email)
                    .build();

            // ID가 지정된 경우 리플렉션으로 강제 주입
            if (id != null) {
                ReflectionTestUtils.setField(user, "id", id);
            }

            return user;
        }
    }
}
