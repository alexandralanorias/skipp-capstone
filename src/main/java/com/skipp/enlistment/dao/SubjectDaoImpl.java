package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class SubjectDaoImpl implements SubjectDao{
    JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectDaoImpl(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Subject> findAllSubjects() {
        String sql = "SELECT * FROM subjects";
        return jdbcTemplate.query(sql, new SubjectDaoMapper());
    }

    @Override
    public Subject findSubject(String subjectId) {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        return jdbcTemplate.queryForObject(sql, new SubjectDaoMapper(), subjectId);
    }

    @Override
    public Subject create(Subject subject) {
        String sql = "INSERT INTO subjects (subject_id) VALUES (?)";
        jdbcTemplate.update(sql, subject.getSubjectId());
        return subject;
    }

    @Override
    public void delete(String subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        jdbcTemplate.update(sql, subjectId);
    }
}

class SubjectDaoMapper implements RowMapper<Subject>{

    @Override
    public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Subject(
                rs.getString("subject_id")
        );
    }
}