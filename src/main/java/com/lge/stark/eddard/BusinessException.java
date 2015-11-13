package com.lge.stark.eddard;

public class BusinessException extends Exception {

    private static final long serialVersionUID = -6643283749965318214L;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
    }
}