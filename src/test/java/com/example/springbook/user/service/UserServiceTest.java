package com.example.springbook.user.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;


import static com.example.springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.dao.TransientDataAccessResourceException;

import com.example.springbook.user.dao.UserDao;
import com.example.springbook.user.domain.Level;
import com.example.springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
@TransactionConfiguration(defaultRollback = false)
public class UserServiceTest implements UserServiceTestIterface{
    @Autowired
    ApplicationContext context;

    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

    // @Autowired
    // UserService userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    List<User> users;

    // @Before는 독립된 테스트를 보장하기 위해 매 @Test마다 실행됨.
    @Before
    public void setUp(){
        users = Arrays.asList(
            new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "e1@mail.com"),
            new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "e2@mail.com"),
            new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "e3@mail.com"),
            new User("madnit1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "e4@mail.com"),
            new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "e5@mail.com")
        );
    }
    // 테스트 클래스가 잘 동작하는지 확인하기 위해 넣은 테스트
    // @Test
    // public void bean(){
    //     assertThat(this.userService, is(notNullValue()));
    // }

    @Test
    @DirtiesContext
    public void upgradeLevels() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnit1", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));
    }

    @Test
    public void add(){
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);
        
        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception{        
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try{
            this.testUserService.upgradeLevels();
            // 실패를 확인하기 위한 테스트인데 위 함수가 정상 종료되면 실패한 테스트니까 fail로 종료해준다.
            fail("TestUserServiceException expected");
        } catch(TestUserServiceException e){

        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void mockUpgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // mock() 메소드에 구현할 인터페이스를 넣어 목 오브젝트를 만든다.
        UserDao mockUserDao = mock(UserDao.class);
        // getAll()이 호출됐을 때의 리턴값을 지정
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);
        
        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();
        // update()(파라미터와 관계없이)의 호출 횟수를 검사.
        // any(): 파라미터의 내용은 무시하고 호출 횟수만 확인할 수 있다.
        verify(mockUserDao, times(2)).update(any(User.class));
        // update()가 users.get(1)의 값을 인자로 호출된 적 있는지 확인한다.
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));
        
        // ArgumentCaptor : 특정 타입의 정보를 저장하는 클래스. 파라미터의 내부 정보를 확인할 때 사용.
        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        // send()가 2번 호출됐는지 검사하면서, send()가 호출될 때 들어간 파라미터를 캡쳐(저장)함.
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
    }

    @Test
    public void advisorAutoProxyCreator() {
        assertThat(testUserService, instanceOf(java.lang.reflect.Proxy.class));
    }

    @Test(expected = TransientDataAccessResourceException.class)
    public void readOnlyTransactionAttribute(){
        testUserService.getAll();
    }

    @Test
    @Transactional
    @Rollback(true)
    public void transactionSync(){        
        userDao.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    private void checkLevel(User user, Level expectedLevel){
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }

    private void checkLevelUpgraded(User user, boolean upgraded){
        User userUpdate = userDao.get(user.getId());
        if(upgraded){
            assertThat(userUpdate.getLevel(),is(user.getLevel().nextLevel()));
        }
        else{
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel){
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    static class TestUserService extends UserServiceImpl {
        private String id = "madnit1";
        
        protected void upgradeLevel(User user){
            if(user.getId().equals(id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
        // @Transactional
        public List<User> getAll(){
            for(User user : super.getAll()) {
                // 읽기 전용인 getAll에서 강제로 update를 발생시킨다.
                super.update(user);
            }
            return null;
        }
    }
}

class TestUserServiceException extends RuntimeException{ }

class MockMailSender implements MailSender{
    private List<String> requests = new ArrayList<String>();

    public List<String> getRequests() {
        return requests;
    }

    public void send(SimpleMailMessage mailMessage) throws MailException {
        requests.add(mailMessage.getTo()[0]);
    }

    public void send(SimpleMailMessage[] mailMessages) throws MailException{
        requests.add(mailMessages[0].getTo()[0]);
    }
}

class MockUserDao implements UserDao{
    private List<User> users;
    private List<User> updated = new ArrayList();

    MockUserDao(List<User> users){
        this.users = users;
    }

    public List<User> getUpdated(){
        return this.updated;
    }

    public List<User> getAll(){
        return this.users;
    }

    public void update(User user){
        updated.add(user);
    }

    public void add(User user) { throw new UnsupportedOperationException(); }
    public void deleteAll() { throw new UnsupportedOperationException(); }
    public User get(String id) { throw new UnsupportedOperationException(); }
    public int getCount() { throw new UnsupportedOperationException(); }
}