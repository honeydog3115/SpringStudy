package com.example.springbook.learningtest.factoryBean;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
    String text;

    public void setText(String text){
        this.text = text;
    }

    // 실제 빈으로 사용될 오브젝트를 직접 생성한다. 코드를 이용하기 때문에 복잡한 방식의 오브젝트 생성과 초기화 작업도 가능
    @Override
    public Message getObject() throws Exception{
        return Message.newMessage(this.text);
    }

    @Override
    public Class<? extends Message> getObjectType() {
        return Message.class;
    }

    // getObject가 돌려주는 오브젝트가 싱글톤인지 알려준다. 이 팩토리빈은 매번 요청할 때마다 새로운 오브젝트를 만들기 떄문에 false
    // 싱글톤으로 할지 안할지는 사용자가 설정할 수 있으며, 만들어진 빈 오브젝트는 싱글톤으로 관리해 줄 수 있
    @Override
    public boolean isSingleton() {
        return false;
    }
}
