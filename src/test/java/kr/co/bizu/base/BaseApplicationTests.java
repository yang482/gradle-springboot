package kr.co.bizu.base;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class BaseApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Test
    void contextLoads() {
    }

    @Test
    void jdbcTests() {
        System.out.println(jdbcTemplate);
    
        jdbcTemplate.queryForMap("select 1 a");
    }
}
