package shortener.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import shortener.models.Users;

@Component
public class UsersDAO {
    private JdbcTemplate jdbcTemplate1;

    @Autowired
    public UsersDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate1 = jdbcTemplate;
    }

    public Users findByUserName(String userName) {
        return jdbcTemplate1.query("select * from users where username=?", new Object[]{userName},
                        new BeanPropertyRowMapper<>(Users.class))
                .stream().findAny().orElse(null);
    }
}
