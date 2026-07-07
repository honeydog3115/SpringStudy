package com.example.springbook.user.sqlservice;

public class SqlUpdateFailureException extends RuntimeException {
    public SqlUpdateFailureException(Throwable cause){
        super(cause);
    }
    public SqlUpdateFailureException(String message){
        super(message);
    }
    // cause는 실패한 근본 원인을 담을 변수
    public SqlUpdateFailureException(String message, Throwable cause){
        super(message, cause);
    }
}
