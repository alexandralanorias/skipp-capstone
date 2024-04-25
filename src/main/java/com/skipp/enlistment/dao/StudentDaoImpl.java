package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
@Repository
public class StudentDaoImpl implements StudentDao{

    JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDaoImpl(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public Collection<Student> findAllStudents() {
        String sql = "SELECT * FROM students";
        return jdbcTemplate.query(sql, new StudentDaoMapper());
    }

    @Override
    public Student findByNumber(int studentNumber) {
        String sql = "SELECT * FROM students WHERE student_number = ?";
        return jdbcTemplate.queryForObject(sql, new StudentDaoMapper(), studentNumber);
    }

    @Override
    public Student create(Student student) {
        String sql = "INSERT INTO students (last_name, first_name) VALUES (?, ?)";
        jdbcTemplate.update(sql, student.getLastName(), student.getFirstName());
        return student;
    }

    @Override
    public Student update(Student student) {
        String sql = "UPDATE students SET last_name = ?, first_name = ? WHERE student_number = ?";
        jdbcTemplate.update(sql, student.getLastName(), student.getFirstName(), student.getStudentNumber());
        return student;
    }

    @Override
    public void delete(int studentNumber) {
        String sql = "DELETE FROM students WHERE student_number = ?";
        jdbcTemplate.update(sql, studentNumber);
    }
}

class StudentDaoMapper implements RowMapper<Student>{

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Student(
                rs.getInt("student_number"),
                rs.getString("first_name"),
                rs.getString("last_name")
        );
    }
}