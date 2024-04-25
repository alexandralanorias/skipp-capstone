package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.dto.FacultyDto;
import com.skipp.enlistment.service.FacultyService;
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
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;
    private final AppUserValidator appUserValidator;

    // TODO What bean should be wired here?
    @Autowired

    public FacultyController(FacultyService facultyService, AppUserValidator appUserValidator){
        this.facultyService = facultyService;
        this.appUserValidator = appUserValidator;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Faculty> getAllFaculty() {
        // TODO implement this handler
        return facultyService.findAllFaculty();
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping("/{facultyNumber}")
    @ResponseStatus(HttpStatus.OK)
    // Hint: The method argument should give you an idea how it would look like.
    public FacultyDto getFaculty(@PathVariable Integer facultyNumber, Authentication auth) {
        // TODO implement this handler
        Faculty faculty;
        appUserValidator.facultyRoleValidator(auth);

        try {
            faculty = facultyService.findByNumber(facultyNumber, true);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Faculty Number: %s not found", facultyNumber));
        }

        return new FacultyDto(faculty,true);
    }

    // TODO What @XXXMapping annotation should be put here?
    @PostMapping
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.CREATED)
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public Faculty createFaculty(@RequestBody Faculty faculty, Authentication auth) {
        // TODO implement this handler
        Faculty newFaculty;
        appUserValidator.facultyRoleValidator(auth);

        try {
            newFaculty = facultyService.create(faculty);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException(String.format("Faculty Number: %s already exists", faculty.getFacultyNumber()));
        }

        return newFaculty;
    }

    // TODO What @XXXMapping annotation should be put here?
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public Faculty updateFaculty(@RequestBody Faculty faculty, Authentication auth) {
        // TODO implement this handler
        Faculty updatedFaculty;
        appUserValidator.facultyRoleValidator(auth);
        try {
            updatedFaculty = facultyService.update(faculty);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Faculty Number: %s not found", faculty.getFacultyNumber()));
        }

        return updatedFaculty;
    }

    // TODO What @XXXMapping annotation should be put here?
    @DeleteMapping("/{facultyNumber}")
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public void deleteFaculty(@PathVariable Integer facultyNumber) {
        // TODO implement this handler

        try {
            facultyService.delete(facultyNumber);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {
            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Faculty Number: %s not found", facultyNumber));
            }

            throw new ReferentialIntegrityViolationException("Faculty " + facultyNumber + " is still teaching a section");
        }
    }

}
