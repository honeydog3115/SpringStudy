package com.example.springbook.learningtest.jdk;

import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

public class ReflectionTest {
    @Test
    public void invokeMethod() throws Exception{
        String name = "Spring";
        assertThat(name.length(), is(6));
        assertThat(name.charAt(0), is('S'));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character)charAtMethod.invoke(name,0), is('S'));
    }

}
