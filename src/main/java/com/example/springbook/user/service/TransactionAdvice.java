package com.example.springbook.user.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor{
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    // 타깃을 호출하는 기능을 가진 콜백 오브젝트를 프록시로부터 받기 때문에 어드바이스는 특정 타깃에 의존하지 않고 재사용 가능
    public Object invoke(MethodInvocation invocation) throws Throwable{
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            // 콜백을 호출해서 타깃의 메소드를 실행. 경우에 ㄸ라 타깃이 아예 호출되지 않거나 재시도를 위한 반복적인 호출도 가능.
            Object ret = invocation.proceed();
            this.transactionManager.commit(status);
            return ret;
        } catch (RuntimeException e) {  // MethodInvocation을 통한 타깃 호출은 예외가 포장되지 않고 타깃에서 보낸 그대로 전달됨.
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
