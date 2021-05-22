package me.parker.springtransaction.repository;

import me.parker.springtransaction.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
public class UserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userRepository = new UserRepository(this.jdbcTemplate);
    }

    @Test
    @DisplayName("미리 생성한 PARK 이름의 유저를 id 를 통해 조회한다. (id = 1)")
    void findById() {
        User user = userRepository.findById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("PARK");
        assertThat(user.getAge()).isEqualTo(20);
    }

    @Test
    @DisplayName("요청한 id 에 속하는 유저의 이름을 변경한다.")
    void update_name() {
        userRepository.updateNameById("KIM", 1L);

        User updatedUser = userRepository.findById(1L);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getName()).isEqualTo("KIM");
        assertThat(updatedUser.getAge()).isEqualTo(20);
    }

    @Test
    @DisplayName("요청한 id 에 속하는 유저의 나이를 변경한다.")
    void update_age() {
        userRepository.updateAgeById(21, 1L);

        User updatedUser = userRepository.findById(1L);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getName()).isEqualTo("PARK");
        assertThat(updatedUser.getAge()).isEqualTo(21);
    }

    @Test
    @DisplayName("요청한 id 에 속하는 유저의 이름과 나이를 변경한다.")
    void update_name_age() {
        userRepository. updateById("KIM", 21, 1L);

        User updatedUser = userRepository.findById(1L);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getName()).isEqualTo("KIM");
        assertThat(updatedUser.getAge()).isEqualTo(21);
    }
}
