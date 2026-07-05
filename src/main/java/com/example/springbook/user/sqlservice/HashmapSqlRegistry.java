package com.example.springbook.user.sqlservice;

import java.util.HashMap;
import java.util.Map;

public class HashmapSqlRegistry implements SqlRegistry{
    private Map<String, String> sqlMap = new HashMap<String,String>();

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null){
            throw new SqlNotFoundException(key + "를 이용해서 찾을 수 없습니다.");
        }
        else return sql;
    }

    @Override
    public void registerSql(java.lang.String key, java.lang.String sql) {
        sqlMap.put(key, sql); 
    }
}