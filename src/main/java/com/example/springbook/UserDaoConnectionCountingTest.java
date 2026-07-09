// package com.example.springbook;

// import java.sql.SQLException;

// import org.springframework.context.ApplicationContext;
// import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// import com.example.springbook.user.dao.CountingConnectionMaker;
// import com.example.springbook.user.dao.CountingDaoFactory;
// import com.example.springbook.user.dao.UserDao;
// import com.example.springbook.user.domain.User;

// public class UserDaoConnectionCountingTest {
//     public static void main(String[] args) throws ClassNotFoundException, SQLException {
//         ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
//         UserDao dao = context.getBean("userDao", UserDao.class);
        
//         User user = new User();

//         user.setId("honeydog");
//         user.setName("선종범");
//         user.setPassword("3115");

//         dao.add(user);

//         user.setId("honeydog2");
//         user.setName("선종범2");
//         user.setPassword("31152");

//         dao.add(user);

//         user.setId("honeydog3");
//         user.setName("선종범3");
//         user.setPassword("31153");
        
//         dao.add(user);

//         CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
//         System.out.println("Connection counter : " + ccm.getCounter());

//     }
// }
