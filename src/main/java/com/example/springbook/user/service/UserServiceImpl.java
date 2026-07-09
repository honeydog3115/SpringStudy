package com.example.springbook.user.service;

import java.sql.Connection;
import javax.sql.DataSource;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.springbook.user.dao.UserDao;
import com.example.springbook.user.domain.Level;
import com.example.springbook.user.domain.User;

// @Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Autowired
    UserDao userDao;
    @Autowired
    MailSender mailSender;
    DataSource dataSource;

    // UserLevelUpgradePolicy userLevelUpgradePolicy;
    
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    // public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy){
    //     this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    // }
    @Override
    public void deleteAll() { userDao.deleteAll(); }
    @Override
    public User get(String id) { return userDao.get(id); }
    @Override
    public List<User> getAll() { return userDao.getAll(); }
    @Override
    public void update(User user) { userDao.update(user); }

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
            for(User user : users){
                if(canUpgradeLevel(user)){
                    upgradeLevel(user);
                }
            }
    }

    private boolean canUpgradeLevel(User user){
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    protected void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMail(user);
    }

    public void add(User user){
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    private void sendUpgradeEMail(User user){    
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

        this.mailSender.send(mailMessage);
    }
    
}