package me.parker.springtransaction.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BankStatement {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private long amount;

    @Builder
    public BankStatement(Long id, Long fromUserId, Long toUserId, long amount) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }

    @Builder
    public BankStatement(Long fromUserId, Long toUserId, long amount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }
}
