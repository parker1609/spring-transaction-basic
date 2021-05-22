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
}
