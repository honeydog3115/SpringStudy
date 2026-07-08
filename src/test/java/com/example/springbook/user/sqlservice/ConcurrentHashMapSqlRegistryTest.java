package com.example.springbook.user.sqlservice;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlReistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
