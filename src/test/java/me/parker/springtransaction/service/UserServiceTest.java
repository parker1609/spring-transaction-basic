package me.parker.springtransaction.service;

import me.parker.springtransaction.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("서비스 데모 테스트")
    void findById() {
        User user = userService.findById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("PARK");
        assertThat(user.getAge()).isEqualTo(20);
    }
}
