package com.example.springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.springbook.user.domain.User;

/*
* Class.forName("com.mysql.jdbc.Driver"); 를 사용하는 이유는
* 데이터베이스에 연결을 설정하기 전에 사용하려는 데이터베이스의 JDBC 드라이버를
* DriverManager에 등록해야 하는데 위 코드를 통해서 등록을 함.
* 하지만 최근에는 위 코드 없이도 등록이 가능함.
*/
// Class.forName("com.mysql.jdbc.Driver");
public interface UserDao {
    public void add(final User user);
    public User get(String id);
    public void deleteAll();
    public int getCount();
    public List<User> getAll();
    public void update(User user);
}