package com.example.springbook.user.sqlservice;

public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException(String message){
        super(message);
    }
    // cause는 실패한 근본 원인을 담을 변수
    public SqlNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}

