package com.example.springbook.learningtest.spring.pointcut;

public class Bean {
    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        System.out.println(Target.class.getMethod("minus", int.class, int.class));
    }

    

    public void method() throws RuntimeException {}
}