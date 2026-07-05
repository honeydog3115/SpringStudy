package com.example.springbook.user.sqlservice;

import java.io.InputStream;

import com.example.springbook.user.dao.UserDao;
import com.example.springbook.user.sqlservice.jaxb.SqlType;
import com.example.springbook.user.sqlservice.jaxb.Sqlmap;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class JaxbXmlSqlReader implements SqlReader{
    private static final String DEFAULt_SQLMAP_FILE = "sqlmap.xml";

    private String sqlmapFile = DEFAULt_SQLMAP_FILE;

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

            for(SqlType sql : sqlmap.getSql()){
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
