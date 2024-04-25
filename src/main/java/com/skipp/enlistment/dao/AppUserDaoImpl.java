package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AppUserDaoImpl implements AppUserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AppUserDaoImpl(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public AppUser create (AppUser user){
        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPasswordHash(), user.getRole());
        return user;
    }

    @Override
    public AppUser findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new AppUserMapper(), username);
    }

    @Override
    public AppUser update(AppUser user) {
        String sql = "UPDATE users SET password_hash = ?, role = ? WHERE username = ?";
        jdbcTemplate.update(sql, user.getPasswordHash(), user.getRole(), user.getUsername());
        return user;
    }
}

class AppUserMapper implements RowMapper<AppUser> {
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AppUser(
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("role"));
    }
}
