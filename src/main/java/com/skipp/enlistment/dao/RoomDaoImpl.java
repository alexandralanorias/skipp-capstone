package com.skipp.enlistment.dao;

import com.skipp.enlistment.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
@Repository
public class RoomDaoImpl implements RoomDao{

    JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomDaoImpl(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Room> findAllRooms() {
        String sql = "SELECT * FROM rooms";
        return jdbcTemplate.query(sql, new RoomDaoWrapper());
    }

    @Override
    public Room findByName(String name) {
        String sql = "SELECT * FROM rooms WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, new RoomDaoWrapper(), name);
    }

    @Override
    public Room create(Room room) {
        String sql = "INSERT INTO rooms (name, capacity) VALUES (?, ?)";
        jdbcTemplate.update(sql, room.getName(), room.getCapacity());
        return room;
    }

    @Override
    public Room update(Room room) {
        String sql = "UPDATE rooms SET capacity = ? WHERE name = ?";
        jdbcTemplate.update(sql, room.getCapacity(), room.getName());
        return room;
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM rooms WHERE name = ?";
        jdbcTemplate.update(sql, name);
    }
}

class RoomDaoWrapper implements RowMapper<Room>{

    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Room(
                rs.getString("name"),
                rs.getInt("capacity")
        );
    }
}