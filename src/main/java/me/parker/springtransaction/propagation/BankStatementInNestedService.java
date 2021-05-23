package me.parker.springtransaction.propagation;

import lombok.RequiredArgsConstructor;
import me.parker.springtransaction.domain.BankStatement;
import me.parker.springtransaction.repository.BankStatementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BankStatementInNestedService implements BankStatementService {

    private final BankStatementRepository bankStatementRepository;

    @Override
    @Transactional(rollbackFor = RollbackPointException.class, propagation = Propagation.NESTED)
    public void save(BankStatement bankStatement, RollbackPoint rollbackPoint) {
        bankStatementRepository.insert(bankStatement);

        if (RollbackPoint.AFTER_INSERT_BANK_STATEMENT == rollbackPoint) {
            throw new RollbackPointException(RollbackPoint.getExceptionMessage(rollbackPoint));
        }
    }
}
