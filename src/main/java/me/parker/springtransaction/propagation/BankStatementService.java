package me.parker.springtransaction.propagation;

import me.parker.springtransaction.domain.BankStatement;

public interface BankStatementService {
    void save(BankStatement bankStatement, RollbackPoint rollbackPoint);
}
