package me.parker.springtransaction.propagation;

public class RollbackPointException extends RuntimeException {

    public RollbackPointException(String message) {
        super(message);
    }
}
