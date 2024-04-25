package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.RoomDao;
import com.skipp.enlistment.domain.Room;
import com.skipp.enlistment.domain.RoomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    private final RoomDao roomRepo;
    private final RoomValidator roomValidator;

    @Autowired
    public RoomServiceImpl (RoomDao roomRepo, RoomValidator roomValidator) {
        this.roomRepo = roomRepo;
        this.roomValidator = roomValidator;
    }

    @Override
    public Collection<Room> findAllRooms() {
        return roomRepo.findAllRooms();
    }

    @Override
    public Room findByName(String name) {
        return roomRepo.findByName(name);
    }

    @Override
    public Room create(Room room) {
        roomValidator.roomNameValidator(room.getName());
        roomValidator.roomCapacityValidator(room.getCapacity());

        return roomRepo.create(room);
    }

    @Override
    public Room update(Room room) {
        roomValidator.roomCapacityValidator(room.getCapacity());
        roomRepo.findByName(room.getName());

        return roomRepo.update(room);
    }

    @Override
    public void delete(String name) {
        roomRepo.findByName(name);
        roomRepo.delete(name);
    }
}
