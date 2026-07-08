package com.example.springbook.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.springbook.user.dao.UserDao;
import com.example.springbook.user.sqlservice.jaxb.SqlType;
import com.example.springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.annotation.PostConstruct;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader{
    private Map<String, String> sqlMap = new HashMap<String, String>();
    private String sqlmapFile;
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    public XmlSqlService(){
        
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public String findSql(String key) throws SqlNotFoundException{
        String sql = sqlMap.get(key);
        if (sql == null) throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        else return sql;
    }

    public void registerSql(String key, String sql){
        sqlMap.put(key, sql);
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // UserDao와 같은 클래스패스의 sqlmap.xml 파일을 변환한다.
            InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);
            System.out.println(sqlMap);
            
            for(SqlType sql : sqlmap.getSql()){
                // 기존 코드는 sqlMap을 호출해서 직접 넣는 방식이었지만
                // 이제는 sqlRegistry의 요소이므로 이를 통해서 접근한다.
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void loadSql(){
        System.out.println("진입");
        this.sqlReader.read(this.sqlRegistry);
    }
    
    public String getSql(String key) throws SqlRetrievalFailureException{
        try {
            return this.sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        }
    }
}