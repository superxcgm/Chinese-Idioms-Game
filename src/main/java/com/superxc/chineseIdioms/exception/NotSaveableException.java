package com.superxc.chineseIdioms.exception;

public class NotSaveableException extends RuntimeException {
    public NotSaveableException() {
    }

    public NotSaveableException(String message) {
        super(message);
    }
}
