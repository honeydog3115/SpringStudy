package com.example.springbook.learningtest.jdk.jaxb;

import com.example.springbook.user.sqlservice.jaxb.*;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class JaxbTest {
    @Test
    public void readSqlmap() throws JAXBException, IOException{
        // 바인딩할 클래스의 위치를 가지고 JAXBContext를 만든다.
        String contextPath = Sqlmap.class.getPackage().getName();
        JAXBContext context = JAXBContext.newInstance(contextPath);

        // 만들어진 JAXBContext로 언마샬러를 생성하고 그걸로 xml파일을 읽어와 바인딩 클래스의 객체로 만든다.
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(getClass().getResourceAsStream("sqlmap.xml"));
        List<SqlType> sqlList = sqlmap.getSql();

        assertThat(sqlList.size(), is(3));
        assertThat(sqlList.get(0).getKey(), is("add"));
        assertThat(sqlList.get(0).getValue(), is("insert"));
        assertThat(sqlList.get(1).getKey(), is("get"));
        assertThat(sqlList.get(1).getValue(), is("select"));
        assertThat(sqlList.get(2).getKey(), is("delete"));
        assertThat(sqlList.get(2).getValue(), is("delete"));
    }
}
