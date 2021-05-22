package me.parker.springtransaction.propagation;

import me.parker.springtransaction.domain.User;
import me.parker.springtransaction.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
//@Transactional
public class TransactionPropagationTest {

    private static final Long USER_ID = 1L;

    @Autowired
    private AService aService;

    @Autowired
    private UserService userService;

    @AfterEach
    void tearDown() {
        userService.updateById(1L, "PARK", 20);
    }

    @Test
    @DisplayName("A 트랜잭션 = REQUIRED, B 트랜잭션 = REQUIRED")
    void required_required() {
        // 초기 유저 정보 확인
        User user = userService.findById(USER_ID);
        checkUser(user, "PARK", 20);

        // B 트랜잭션 이전 시점에서 롤백이 발생한 경우
        try {
            aService.update(USER_ID, RollbackPoint.BEFORE_NESTED_TRANSACTION);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.BEFORE_NESTED_TRANSACTION));
        } catch (RollbackPointException e) {
        }

        user = userService.findById(USER_ID);
        checkUser(user, "PARK", 20);

        // B 트랜잭션 내부 시점에서 롤백이 발생한 경우
        try {
            aService.update(USER_ID, RollbackPoint.INNER_NESTED_TRANSACTION);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.INNER_NESTED_TRANSACTION));
        } catch (RollbackPointException e) {
        }

        user = userService.findById(USER_ID);
        checkUser(user, "PARK", 20);

        // B 트랜잭션 외부 시점에서 롤백이 발생한 경우
        try {
            aService.update(USER_ID, RollbackPoint.AFTER_NESTED_TRANSACTION);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_NESTED_TRANSACTION));
        } catch (RollbackPointException e) {
        }

        user = userService.findById(USER_ID);
        checkUser(user, "PARK", 20);
    }

    private void checkUser(User user, String name, int age) {
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getAge()).isEqualTo(age);
    }
}
