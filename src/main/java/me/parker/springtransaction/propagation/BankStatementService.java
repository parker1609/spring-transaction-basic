package me.parker.springtransaction.propagation;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.domain.BankStatement;
import me.parker.springtransaction.repository.BankStatementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BankStatementService {

    private final BankStatementRepository bankStatementRepository;

    @Transactional(readOnly = true)
    public BankStatement findById(Long bankStatementId) {
        return bankStatementRepository.findById(bankStatementId);
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void save(Long fromUserId, Long toUserId, long amount, RollbackPoint rollbackPoint) {
        bankStatementRepository.insert(fromUserId, toUserId, amount);

        if (RollbackPoint.AFTER_INSERT_BANK_STATEMENT == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRES_NEW)
    public void saveRequiresNew(Long fromUserId, Long toUserId, long amount, RollbackPoint rollbackPoint) {
        bankStatementRepository.insert(fromUserId, toUserId, amount);

        if (RollbackPoint.AFTER_INSERT_BANK_STATEMENT == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.NESTED)
    public void saveNested(Long fromUserId, Long toUserId, long amount, RollbackPoint rollbackPoint) {
        bankStatementRepository.insert(fromUserId, toUserId, amount);

        if (RollbackPoint.AFTER_INSERT_BANK_STATEMENT == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }
}
