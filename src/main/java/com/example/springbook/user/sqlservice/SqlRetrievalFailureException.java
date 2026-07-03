package com.example.springbook.user.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {
    public SqlRetrievalFailureException(String message){
        super(message);
    }
    // cause는 실패한 근본 원인을 담을 변수
    public SqlRetrievalFailureException(String message, Throwable cause){
        super(message, cause);
    }
}
