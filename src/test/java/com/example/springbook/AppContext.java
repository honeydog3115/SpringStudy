package com.example.springbook;

import com.example.springbook.user.dao.UserDao;
import com.example.springbook.user.service.DummyMailSender;
import com.example.springbook.user.service.UserService;
import com.example.springbook.user.service.UserServiceTest.TestUserService;
import com.example.springbook.user.sqlservice.SqlMapConfig;
import com.mysql.jdbc.Driver;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="com.example.springbook.user")
@EnbaleSqlService
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig {
    // @Autowired
    // Environment env;

    @Value("${db.driverClass}") Class<? extends Driver> driverClass;
    @Value("${db.url}") String url;
    @Value("${db.username}") String username;
    @Value("${db.password}") String password;

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml",UserDao.class);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(this.driverClass);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    @Configuration
    @Profile("production")
    static public class ProductionAppContext {
        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("mail.mycompany.com");
            return mailSender;
        }
    }
    
    @Configuration
    @Profile("test")
    static public class TestAppContext {
        @Bean 
        public UserService testUserService(){
            TestUserService testUserService = new TestUserService();

            return testUserService;
        }

        @Bean
        public MailSender mailSender(){
            DummyMailSender mailSender = new DummyMailSender();

            return mailSender;
        }
    }
}
