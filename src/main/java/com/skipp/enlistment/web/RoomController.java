package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

// TODO What stereotype annotation should be put here?
@RestController
@RequestMapping("/rooms")
public class RoomController {

    private RoomService roomService;
    private AppUserValidator appUserValidator;

    // TODO What bean should be wired here?
    @Autowired
    public RoomController(RoomService roomService, AppUserValidator appUserValidator){
        this.roomService = roomService;
        this.appUserValidator = appUserValidator;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Room> getRooms() {
        // TODO implement this handler
        return roomService.findAllRooms();
    }

    // TODO What @XXXMapping annotation should be put here?
    @PostMapping
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.CREATED)
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public Room createRoom(@RequestBody Room room, Authentication auth) {
        // TODO implement this handler
        Room newRoom;
        appUserValidator.facultyRoleValidator(auth);

        try{
            newRoom = roomService.create(room);
        }catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException(String.format("Room Name: %s not found", room.getName()));
        }
        return newRoom;

    }

    // TODO What @XXXMapping annotation should be put here?
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public Room updateRoom(@RequestBody Room room, Authentication auth) {
        // TODO implement this handler
        Room updatedRoom;
        appUserValidator.facultyRoleValidator(auth);

        try{
            updatedRoom = roomService.update(room);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Room Name: %s not found", room.getName()));
        }

        return updatedRoom;
    }

    // TODO What @XXXMapping annotation should be put here?
    @DeleteMapping("/{roomName}")
    @ResponseStatus(HttpStatus.OK)
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public void deleteRoom(@PathVariable String roomName, Authentication auth) {
        // TODO implement this handler
        appUserValidator.facultyRoleValidator(auth);

        try{
            roomService.delete(roomName);
        }catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Room Name: %s not found", roomName));
            }

            throw new ReferentialIntegrityViolationException("Room " + roomName + " is still being used.");
        }
    }

}
