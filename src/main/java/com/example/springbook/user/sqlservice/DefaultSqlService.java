package com.example.springbook.user.sqlservice;

public class DefaultSqlService extends BaseSqlService{
    public DefaultSqlService(){
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashmapSqlRegistry());
    }
}