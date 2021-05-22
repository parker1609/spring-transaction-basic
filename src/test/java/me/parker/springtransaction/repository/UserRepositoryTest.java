package me.parker.springtransaction.repository;

import me.parker.springtransaction.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
    @DisplayName("id = 1 인 사용자 조회")
    void findById() {
        User user = userRepository.findById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("PARK");
        assertThat(user.getBalance()).isEqualTo(1000);
    }

    @Test
    @DisplayName("요청한 id 에 속하는 유저의 이름을 변경한다.")
    void update_name() {
        userRepository.updateNameById("LEE", 1L);

        User updatedUser = userRepository.findById(1L);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getName()).isEqualTo("LEE");
        assertThat(updatedUser.getBalance()).isEqualTo(1000);
    }

    @Test
    @DisplayName("요청한 id 에 속하는 유저의 잔액을 변경한다.")
    void update_age() {
        userRepository.updateBalanceById(2000, 1L);

        User updatedUser = userRepository.findById(1L);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getName()).isEqualTo("PARK");
        assertThat(updatedUser.getBalance()).isEqualTo(2000);
    }

    @Test
    @DisplayName("요청한 id 에 속하는 유저의 이름과 잔액을 변경한다.")
    void update_name_age() {
        userRepository. updateById("LEE", 2000, 1L);

        User updatedUser = userRepository.findById(1L);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getName()).isEqualTo("LEE");
        assertThat(updatedUser.getBalance()).isEqualTo(2000);
    }
}
