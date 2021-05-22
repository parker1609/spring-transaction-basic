package me.parker.springtransaction.propagation;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BankingService {

    private final UserRepository userRepository;
    private final BankStatementService bankStatementService;

    public void bankingInNoTransaction(Long fromUserId, Long toUserId, long amount, RollbackPoint rollbackPoint) {
        userRepository.updateBalanceById(0, fromUserId);

        if (RollbackPoint.AFTER_DEBITS == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        userRepository.updateBalanceById(2000, toUserId);

        bankStatementService.save(fromUserId, toUserId, amount, rollbackPoint);
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void banking(Long fromUserId, Long toUserId, long amount, RollbackPoint rollbackPoint) {
        userRepository.updateBalanceById(0, fromUserId);

        if (RollbackPoint.AFTER_DEBITS == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        userRepository.updateBalanceById(2000, toUserId);

        bankStatementService.save(fromUserId, toUserId, amount, rollbackPoint);
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void bankingCallRequiresNew(Long fromUserId, Long toUserId, long amount, RollbackPoint rollbackPoint) {
        userRepository.updateBalanceById(0, fromUserId);

        if (RollbackPoint.AFTER_DEBITS == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        userRepository.updateBalanceById(2000, toUserId);

        bankStatementService.saveRequiresNew(fromUserId, toUserId, amount, rollbackPoint);
    }

    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.REQUIRED)
    public void bankingCallNested(Long fromUserId, Long toUserId, long amount, RollbackPoint rollbackPoint) {
        userRepository.updateBalanceById(0, fromUserId);

        if (RollbackPoint.AFTER_DEBITS == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }

        userRepository.updateBalanceById(2000, toUserId);

        bankStatementService.saveNested(fromUserId, toUserId, amount, rollbackPoint);
    }
}
