package com.example.springbook.learningtest.factoryBean;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;


import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/FactoryBeanTest-context.xml")
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;

    @Test
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        // Message.class는 Class 타입의 객체이고 message는 Message 객체이므로 getClass() 해줘야 비교가됨.
        assertThat(message, instanceOf(Message.class));
        assertThat(((Message)message).getText(), is("Factory Bean"));
    }

    @Test
    public void getFactoryBean() throws Exception {
        Object factory = context.getBean("&message");
        assertThat(factory, instanceOf(MessageFactoryBean.class));
    }
}
