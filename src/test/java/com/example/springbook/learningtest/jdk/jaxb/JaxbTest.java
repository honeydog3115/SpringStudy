package com.example.springbook.learningtest.jdk.jaxb;

import com.example.springbook.user.sqlservice.jaxb.*;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import java.io.IOException;
// import java.sql.SQLType;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

// import jakarta.xml.bind.JAXB;
// import jakarta.xml.bind.JAXBContext;
// import jakarta.xml.bind.JAXBException;
// import jakarta.xml.bind.Unmarshaller;

import static org.hamcrest.CoreMatchers.is;

public class JaxbTest {
    @Test
    public void readSqlmap() throws JAXBException, IOException{
        String contextPath = Sqlmap.class.getPackage().getName();
        System.err.println(contextPath);
        JAXBContext context = JAXBContext.newInstance(contextPath);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
            getClass().getResourceAsStream("sqlmap.xml"));
        List<SqlType> sqlList = sqlmap.getSql();

        assertThat(sqlList.size(), is(3));
    }
}
