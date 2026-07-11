package com.example.springbook;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.example.springbook.user.sqlservice.EmbeddedDbSqlRegistry;
import com.example.springbook.user.sqlservice.OxmSqlService;
import com.example.springbook.user.sqlservice.SqlMapConfig;
import com.example.springbook.user.sqlservice.SqlRegistry;
import com.example.springbook.user.sqlservice.SqlService;

@Configuration
public class SqlServiceContext {
    @Autowired
    SqlMapConfig sqlMapConfig;

    @Bean
    public SqlService sqlService(){
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setSqlRegistry(sqlRegistry());
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlmap(this.sqlMapConfig.getSqlMapResource());

        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry(){
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());

        return sqlRegistry;
    }
    
    @Bean
    public Unmarshaller unmarshaller(){
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setContextPath("com.example.springbook.user.sqlservice.jaxb");

        return unmarshaller;
    }

    @Bean
    public DataSource embeddedDatabase(){
        DataSource embeddedDatabase = new EmbeddedDatabaseBuilder()
            .setName("embeddedDatabase")
            .setType(HSQL)
            .addScript("classpath:com/example/springbook/user/sqlservice/sqlRegistrySchema.sql")
            .build();

        return embeddedDatabase;
    }

}
