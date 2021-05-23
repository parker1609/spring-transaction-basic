package me.parker.springtransaction.propagation;

import me.parker.springtransaction.domain.BankStatement;
import me.parker.springtransaction.domain.User;
import me.parker.springtransaction.repository.BankStatementRepository;
import me.parker.springtransaction.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class RequiresNewTransactionTest {

    @Qualifier("requiresNewBankingService")
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
    @DisplayName("1. 출금 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRES_NEW)")
    void _01_rollback_REQUIRED_REQUIRES_NEW_1() {
        try {
            bankingService.banking(bankStatement, RollbackPoint.AFTER_DEBITS);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_DEBITS));
        } catch (RollbackPointException e) {
        }

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("2-1. 거래 내역서 생성 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRES_NEW)")
    void _02_rollback_REQUIRED_REQUIRES_NEW_2_1() {
        try {
            bankingService.banking(bankStatement, RollbackPoint.AFTER_INSERT_BANK_STATEMENT);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_INSERT_BANK_STATEMENT));
        } catch (RollbackPointException e) {
        }

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("2-2. 거래 내역서 생성 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRES_NEW)")
    void _03_rollback_REQUIRED_REQUIRES_NEW_2_2() {
        bankingService.bankingWithNestedRollbackCatch(bankStatement, RollbackPoint.AFTER_INSERT_BANK_STATEMENT);

        // 바깥 서비스인 출금 및 입금 로직은 롤백되지 않음.
        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(0);
        assertThat(toUser.getBalance()).isEqualTo(2000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("3. 뱅킹 서비스 끝나기 직전 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRES_NEW)")
    void _04_rollback_REQUIRED_REQUIRES_NEW_3() {
        try {
            bankingService.banking(bankStatement, RollbackPoint.BEFORE_END);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.BEFORE_END));
        } catch (RollbackPointException e) {
        }

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        // 내부 서비스인 거래 내역 생성 로직은 롤백되지 않음.
        BankStatement savedBankStatement = bankStatementRepository.findById(2L);
        assertThat(savedBankStatement.getId()).isEqualTo(2L);
        assertThat(savedBankStatement.getFromUserId()).isEqualTo(1L);
        assertThat(savedBankStatement.getToUserId()).isEqualTo(2L);
        assertThat(savedBankStatement.getAmount()).isEqualTo(1000);
    }
}
