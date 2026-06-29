package com.example.springbook.user.service;

import java.sql.Connection;
import javax.sql.DataSource;
import java.util.List;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.springbook.user.dao.UserDao;
import com.example.springbook.user.domain.Level;
import com.example.springbook.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
} 