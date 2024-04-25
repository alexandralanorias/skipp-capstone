package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class FacultyDaoImpl implements FacultyDao {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public FacultyDaoImpl(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Faculty> findAllFaculty() {
        String sql = "SELECT * FROM faculty";
        return jdbcTemplate.query(sql, new FacultyWrapper());
    }

    @Override
    public Faculty findByNumber(int facultyNumber) {
        String sql = "SELECT * FROM faculty WHERE faculty_number = ?";
        return jdbcTemplate.queryForObject(sql, new FacultyWrapper(), facultyNumber);
    }

    @Override
    public Faculty create(Faculty faculty) {
        String sql = "INSERT INTO faculty (faculty_number, first_name, last_name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, faculty.getFacultyNumber(), faculty.getFirstName(), faculty.getLastName());
        return faculty;
    }

    @Override
    public Faculty update(Faculty faculty) {
        String sql = "UPDATE faculty SET first_name = ?, last_name = ? WHERE faculty_number = ?";
        jdbcTemplate.update(sql, faculty.getFirstName(), faculty.getLastName(), faculty.getFacultyNumber());
        return faculty;
    }

    @Override
    public void delete(int facultyNumber) {
        String sql = "DELETE FROM faculty WHERE faculty_number = ?";
        jdbcTemplate.update(sql, facultyNumber);
    }
}

class FacultyWrapper implements RowMapper<Faculty> {

    @Override
    public Faculty mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Faculty(
                rs.getInt("faculty_number"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}