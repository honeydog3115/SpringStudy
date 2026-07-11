package com.example.springbook.user.sqlservice;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import com.example.springbook.user.dao.UserDao;
import com.example.springbook.user.sqlservice.jaxb.SqlType;
import com.example.springbook.user.sqlservice.jaxb.Sqlmap;

public class OxmSqlService implements SqlService{
    private OxmSqlReader oxmSqlReader  = new OxmSqlReader();
    private SqlRegistry sqlRegistry = new HashmapSqlRegistry();
    private final BaseSqlService baserSqlService = new BaseSqlService();
    private Resource sqlmap;

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmap(Resource sqlmap){
        this.oxmSqlReader.setSqlmap(sqlmap);
    }
    
    @PostConstruct
    public void loadSql() { 
        this.baserSqlService.setSqlReader(oxmSqlReader);
        this.baserSqlService.setSqlRegistry(sqlRegistry);

        this.baserSqlService.loadSql();
    }

    public String getSql(String key) throws SqlRetrievalFailureException{
        return this.baserSqlService.getSql(key);
    }

    private class OxmSqlReader implements SqlReader{
        private Unmarshaller unmarshaller;
        private final static String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
        private Resource sqlmap;


        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(this.sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(source);

                for(SqlType sql : sqlmap.getSql()){
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(this.sqlmap.getFilename() + "을 가져올 수 없습니다.", e);
            }    
        }
    }
}
