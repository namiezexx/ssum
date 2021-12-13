package com.kyobo.dev.api.Ssum.advice.exception;

public class CExpiredAccessTokenException extends RuntimeException {
    public CExpiredAccessTokenException(String msg, Throwable t) {
        super(msg, t);
    }

    public CExpiredAccessTokenException(String msg) {
        super(msg);
    }

    public CExpiredAccessTokenException() {
        super();
    }
}