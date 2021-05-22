package me.parker.springtransaction.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class BankStatement {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private long amount;
}
