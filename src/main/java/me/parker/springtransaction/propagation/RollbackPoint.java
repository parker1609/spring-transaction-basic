package me.parker.springtransaction.propagation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RollbackPoint {
    NOTHING("롤백하지 않음"),
    AFTER_DEBITS("출금 이후"),
    AFTER_INSERT_BANK_STATEMENT("거래 내역서 생성 이후")
    ;

    private final String point;

    public static String getExceptionMessage(RollbackPoint rollbackPoint) {
        return rollbackPoint.getPoint() + "시점에 일어난 예외입니다.";
    }
}
