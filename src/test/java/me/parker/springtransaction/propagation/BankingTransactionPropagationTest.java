package me.parker.springtransaction.propagation;

import me.parker.springtransaction.domain.BankStatement;
import me.parker.springtransaction.domain.User;
import me.parker.springtransaction.repository.BankStatementRepository;
import me.parker.springtransaction.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BankingTransactionPropagationTest {

    @Autowired
    private BankingService bankingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Test
    @DisplayName("정상 거래 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRED)")
    void success_REQUIRED_REQUIRED() {
        bankingService.banking(1L, 2L, 1000, RollbackPoint.NOTHING);

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(0);
        assertThat(toUser.getBalance()).isEqualTo(2000);

        BankStatement savedBankStatement = bankStatementRepository.findById(2L);
        System.out.println(savedBankStatement);
        assertThat(savedBankStatement.getId()).isEqualTo(2L);
        assertThat(savedBankStatement.getFromUserId()).isEqualTo(1L);
        assertThat(savedBankStatement.getToUserId()).isEqualTo(2L);
        assertThat(savedBankStatement.getAmount()).isEqualTo(1000);
    }

    @Test
    @DisplayName("트랜잭션 범위가 단일 쿼리인 경우")
    void no_transaction() {
        try {
            bankingService.bankingInNoTransaction(1L, 2L, 1000, RollbackPoint.AFTER_DEBITS);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_DEBITS));
        } catch (RollbackPointException e) {}

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(0);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("출금 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRED)")
    void rollback_REQUIRED_REQUIRED_1() {
        try {
            bankingService.banking(1L, 2L, 1000, RollbackPoint.AFTER_DEBITS);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_DEBITS));
        } catch (RollbackPointException e) {}

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("거래 내역서 생성 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRED)")
    void rollback_REQUIRED_REQUIRED_2() {
        try {
            bankingService.banking(1L, 2L, 1000, RollbackPoint.AFTER_INSERT_BANK_STATEMENT);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_INSERT_BANK_STATEMENT));
        } catch (RollbackPointException e) {}

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("출금 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRES_NEW)")
    void rollback_REQUIRED_REQUIRES_NEW_1() {
        try {
            bankingService.bankingCallRequiresNew(1L, 2L, 1000, RollbackPoint.AFTER_DEBITS);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_DEBITS));
        } catch (RollbackPointException e) {}

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("거래 내역서 생성 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = REQUIRES_NEW)")
    void rollback_REQUIRED_REQUIRES_NEW_2() {
        try {
            bankingService.bankingCallRequiresNew(1L, 2L, 1000, RollbackPoint.AFTER_INSERT_BANK_STATEMENT);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_INSERT_BANK_STATEMENT));
        } catch (RollbackPointException e) {}

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("출금 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = NESTED)")
    void rollback_REQUIRED_NESTED_1() {
        try {
            bankingService.bankingCallNested(1L, 2L, 1000, RollbackPoint.AFTER_DEBITS);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_DEBITS));
        } catch (RollbackPointException e) {}

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }

    @Test
    @DisplayName("거래 내역서 생성 후 롤백 (은행 거래 트랜잭션 = REQUIRED, 거래내역서 생성 트랜잭션 = NESTED)")
    void rollback_REQUIRED_NESTED_2() {
        try {
            bankingService.bankingCallNested(1L, 2L, 1000, RollbackPoint.AFTER_INSERT_BANK_STATEMENT);
            fail(RollbackPoint.getExceptionMessage(RollbackPoint.AFTER_INSERT_BANK_STATEMENT));
        } catch (RollbackPointException e) {}

        User fromUser = userRepository.findById(1L);
        User toUser = userRepository.findById(2L);
        assertThat(fromUser.getBalance()).isEqualTo(1000);
        assertThat(toUser.getBalance()).isEqualTo(1000);

        assertThrows(EmptyResultDataAccessException.class,
                () -> bankStatementRepository.findById(2L));
    }
}
