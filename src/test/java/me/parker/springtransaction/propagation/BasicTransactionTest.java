package me.parker.springtransaction.propagation;

import me.parker.springtransaction.domain.BankStatement;
import me.parker.springtransaction.domain.User;
import me.parker.springtransaction.repository.BankStatementRepository;
import me.parker.springtransaction.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BasicTransactionTest {

    @Autowired
    private BankingService bankingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    private BankStatement bankStatement;

    @BeforeEach
    void setUp() {
        this.bankStatement = BankStatement.builder()
                .fromUserId(1L)
                .toUserId(2L)
                .amount(1000)
                .build();
    }

    @Test
    @DisplayName("트랜잭션 범위가 단일 쿼리인 경우")
    void _01_no_transaction() {
        try {
            bankingService.bankingInNoTransaction(bankStatement, RollbackPoint.AFTER_DEBITS);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_DEBITS));
        } catch (RollbackPointException e) {
        }

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(0);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("정상 거래 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRED)")
    void _02_success_REQUIRED_REQUIRED() {
        bankingService.banking(bankStatement, RollbackPoint.NOTHING);

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(0);
        assertThat(toUser.getBalance()).isEqualTo(2000);

        BankStatement savedBankStatement = bankStatementRepository.findById(2L);
        assertThat(savedBankStatement.getId()).isEqualTo(2L);
        assertThat(savedBankStatement.getFromUserId()).isEqualTo(1L);
        assertThat(savedBankStatement.getToUserId()).isEqualTo(2L);
        assertThat(savedBankStatement.getAmount()).isEqualTo(1000);
    }
}
