package com.example.springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.springbook.user.domain.Level;
import com.example.springbook.user.domain.User;
import com.example.springbook.user.sqlservice.SqlService;

/*
* Class.forName("com.mysql.jdbc.Driver"); 를 사용하는 이유는
* 데이터베이스에 연결을 설정하기 전에 사용하려는 데이터베이스의 JDBC 드라이버를
* DriverManager에 등록해야 하는데 위 코드를 통해서 등록을 함.
* 하지만 최근에는 위 코드 없이도 등록이 가능함.
*/
// Class.forName("com.mysql.jdbc.Driver");
public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private SqlService sqlService;

    private RowMapper<User> userMapper = new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rowNum) throws SQLException{
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("Password"));
                user.setLevel(Level.valueOf(rs.getInt("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));
                user.setEmail(rs.getString("email"));
                return user;
            
        }
    };

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user){
        this.jdbcTemplate.update(
            this.sqlService.getSql("userAdd"),
            user.getId(), user.getName(), user.getPassword(), 
            user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
    }

    public User get(String id){
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), 
        new Object[] {id}, this.userMapper);
    }

    public void deleteAll(){
        this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }

    public int getCount(){
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGetCount"), Integer.class);
    }   

    public List<User> getAll(){
        return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"), this.userMapper);
    }

    public void update(User user){
        this.jdbcTemplate.update(
            this.sqlService.getSql("userUpdate"), 
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
            user.getRecommend(), user.getEmail(), user.getId());
    }
}