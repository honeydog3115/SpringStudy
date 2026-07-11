// package com.example.springbook;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;
// import org.springframework.mail.MailSender;

// import com.example.springbook.user.service.DummyMailSender;
// import com.example.springbook.user.service.UserService;
// import com.example.springbook.user.service.UserServiceTest.TestUserService;

// @Configuration
// @Profile("test")
// public class TestAppContext {
//     @Bean 
//     public UserService testUserService(){
//         TestUserService testUserService = new TestUserService();

//         return testUserService;
//     }

//     @Bean
//     public MailSender mailSender(){
//         DummyMailSender mailSender = new DummyMailSender();

//         return mailSender;
//     }
// }
