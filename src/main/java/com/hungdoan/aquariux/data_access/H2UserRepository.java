package com.hungdoan.aquariux.data_access;

import com.hungdoan.aquariux.data_access.spec.UserRepository;
import com.hungdoan.aquariux.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Repository
public class H2UserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM \"User\" WHERE username = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{username}, new UserRowMapper());
        return users.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM \"User\" WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new Object[]{email}, new UserRowMapper());
        return users.stream().findFirst();
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getInt("version"),
                    rs.getTimestamp("created_at").toInstant().atZone(ZoneId.systemDefault()),
                    rs.getTimestamp("updated_at").toInstant().atZone(ZoneId.systemDefault())
            );
        }
    }
}
