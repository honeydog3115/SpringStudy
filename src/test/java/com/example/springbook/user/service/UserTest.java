package com.example.springbook.user.service;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.springbook.user.domain.Level;
import com.example.springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class UserTest {
    User user;

    @Before
    public void setUp(){
        user = new User();
    }
    
    @Test
    public void upgradeLevel(){
        Level[] levels = Level.values();
        for(Level level: levels){
            if(level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(),is(level.nextLevel()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotUpgrade(){
        Level[] levels = Level.values();
        for(Level level: levels){
            if(level.nextLevel() != null) continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}
