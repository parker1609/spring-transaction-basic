package me.parker.springtransaction.propagation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RollbackPoint {
    BEFORE_NESTED_TRANSACTION("B 트랜잭션 이전"),
    INNER_NESTED_TRANSACTION("B 트랜잭션 내부"),
    AFTER_NESTED_TRANSACTION("B 트랜잭션 이후")
    ;

    private final String point;

    public static String getExceptionMessage(RollbackPoint rollbackPoint) {
        return rollbackPoint.getPoint() + "시점에 일어난 예외입니다.";
    }
}
