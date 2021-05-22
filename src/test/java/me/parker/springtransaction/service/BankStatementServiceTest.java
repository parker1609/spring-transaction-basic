package me.parker.springtransaction.service;

import me.parker.springtransaction.domain.BankStatement;
import me.parker.springtransaction.propagation.BankStatementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BankStatementServiceTest {

    @Autowired
    private BankStatementService bankStatementService;

    @Test
    @DisplayName("id 조회")
    void findById() {
        BankStatement bankStatement = bankStatementService.findById(1L);

        assertThat(bankStatement.getId()).isEqualTo(1L);
        assertThat(bankStatement.getFromUserId()).isEqualTo(2L);
        assertThat(bankStatement.getToUserId()).isEqualTo(3L);
        assertThat(bankStatement.getAmount()).isEqualTo(2000);
    }
}
