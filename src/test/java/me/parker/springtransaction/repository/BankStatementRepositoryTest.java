package me.parker.springtransaction.repository;

import me.parker.springtransaction.domain.BankStatement;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;

@DataJdbcTest
public class BankStatementRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private BankStatementRepository bankStatementRepository;

    @BeforeEach
    void setUp() {
        this.bankStatementRepository = new BankStatementRepository(this.jdbcTemplate);
    }

    @Test
    @DisplayName("id = 1 거래 내역서 조회")
    void findById() {
        BankStatement bankStatement = bankStatementRepository.findById(1L);

        assertThat(bankStatement).isNotNull();
        assertThat(bankStatement.getId()).isEqualTo(1L);
        assertThat(bankStatement.getFromUserId()).isEqualTo(2L);
        assertThat(bankStatement.getToUserId()).isEqualTo(3L);
        assertThat(bankStatement.getAmount()).isEqualTo(2000);
    }

    @Test
    @DisplayName("새로운 거래 내역서를 추가한다.")
    void insert() {
        BankStatement newBankStatement = BankStatement.builder()
                .fromUserId(2L)
                .toUserId(3L)
                .amount(10000)
                .build();
        bankStatementRepository.insert(newBankStatement);

        BankStatement savedBankStatement = bankStatementRepository.findById(2L);

        assertThat(savedBankStatement).isNotNull();
        assertThat(savedBankStatement.getId()).isEqualTo(2L);
        assertThat(savedBankStatement.getFromUserId()).isEqualTo(2L);
        assertThat(savedBankStatement.getToUserId()).isEqualTo(3L);
        assertThat(savedBankStatement.getAmount()).isEqualTo(10000);
    }
}
