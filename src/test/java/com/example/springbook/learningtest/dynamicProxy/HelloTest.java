package com.example.springbook.learningtest.dynamicProxy;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import java.lang.reflect.Proxy;

public class HelloTest {
    @Test
    public void simpleProxy(){
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby"), is("Hello Toby"));
        assertThat(hello.sayHi("Toby"), is("Hi Toby"));
        assertThat(hello.sayThankyou("Toby"), is("Thank you Toby"));

        Hello proxiedHello = (Hello)Proxy.newProxyInstance(
            getClass().getClassLoader(), 
            // 구현할 인터페이스, 여러 인터페이스로 프록시를 만들 수 있어 Class 배열로 받음.
            new Class[] { Hello.class }, 
            // 부가기능과 위임 코드를 담은 invocationHandler
            new UppercaseHandler(new HelloTarget()));
        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
        assertThat(proxiedHello.sayThankyou("Toby"), is("THANK YOU TOBY"));
    }
}
