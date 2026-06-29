package com.example.springbook.learningtest.factoryBean;

public class Message {
    String text;

    // private 생성자이기 떄문에 외부에서 생성자로 오브젝트를 만들 수 없다.
    private Message(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    // 생성자 대신 사용할 수 있는 스태틱 팩토리 메소드를 제공한다.
    public static Message newMessage(String text){
        return new Message(text);
    }
}
