package com.kyobo.dev.api.Ssum.advice.exception;

public class CJwtTokenNotAvailable extends RuntimeException {
    public CJwtTokenNotAvailable(String msg, Throwable t) {
        super(msg, t);
    }

    public CJwtTokenNotAvailable(String msg) {
        super(msg);
    }

    public CJwtTokenNotAvailable() { super(); }
}
