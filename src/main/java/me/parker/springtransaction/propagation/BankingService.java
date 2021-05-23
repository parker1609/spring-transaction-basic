package me.parker.springtransaction.propagation;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.domain.BankStatement;
import me.parker.springtransaction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BankingService {

    private final UserRepository userRepository;
    private final BankStatementService bankStatementService;

    public void bankingInNoTransaction(BankStatement bankStatement, RollbackPoint rollbackPoint) {
        userRepository.updateBalanceById(0, bankStatement.getFromUserId());

        if (RollbackPoint.AFTER_DEBITS == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        userRepository.updateBalanceById(2000, bankStatement.getToUserId());

        bankStatementService.save(bankStatement, rollbackPoint);
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void banking(BankStatement bankStatement, RollbackPoint rollbackPoint) {
        userRepository.updateBalanceById(0, bankStatement.getFromUserId());

        if (RollbackPoint.AFTER_DEBITS == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        userRepository.updateBalanceById(2000, bankStatement.getToUserId());

        bankStatementService.save(bankStatement, rollbackPoint);

        if (RollbackPoint.BEFORE_END == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void bankingWithNestedRollbackCatch(BankStatement bankStatement, RollbackPoint rollbackPoint) {
        userRepository.updateBalanceById(0, bankStatement.getFromUserId());

        if (RollbackPoint.AFTER_DEBITS == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        userRepository.updateBalanceById(2000, bankStatement.getToUserId());

        try {
            bankStatementService.save(bankStatement, rollbackPoint);
        } catch (RollbackPointException e) {
        }

        if (RollbackPoint.BEFORE_END == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }
}
