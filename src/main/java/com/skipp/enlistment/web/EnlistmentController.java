package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.service.EnlistmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// TODO What stereotype annotation should be put here?
@RestController
@RequestMapping("/enlist")
public class EnlistmentController {
    private final EnlistmentService enlistmentService;
    private final AppUserValidator appUserValidator;

    // TODO What bean should be wired here?
    @Autowired

    public EnlistmentController(EnlistmentService enlistmentService, AppUserValidator appUserValidator) {
        this.enlistmentService = enlistmentService;
        this.appUserValidator = appUserValidator;
    }

    // TODO What @XXXMapping annotation should be put here?
    @PostMapping
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    @ResponseStatus(HttpStatus.CREATED)
    // TODO What's the appropriate status code for this handler?
    // Hint: What does the test expect?
    // TODO This should only be accessed by students. Apply the appropriate annotation.
     @PreAuthorize("hasRole('STUDENT')")
    public Enlistment enlist(@RequestBody Enlistment enlistment, Authentication auth) {
        // TODO implement this handler
        // Hint: 'auth' is where you can get the username of the user accessing the API
        Enlistment newEnlistment;
        appUserValidator.studentRoleValidator(auth);
        appUserValidator.studentAuthorization(auth, enlistment.studentNumber());

        try {
            newEnlistment = enlistmentService.enlist(enlistment.studentNumber(),enlistment.sectionId());
        } catch(DuplicateEnlistmentException | SameSubjectException | ScheduleConflictException | RoomCapacityReachedException e){
            throw new EnlistmentException(e.getMessage());
        }
        return newEnlistment;
    }

    // TODO What @XXXMapping annotation should be put here?
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void cancel(@RequestBody Enlistment enlistment) {
        // TODO implement this handler
        try {
            enlistmentService.cancel(enlistment.studentNumber(), enlistment.sectionId());
        } catch(EmptyResultDataAccessException | RecordNotFoundException e) {
            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Enlistment not found: Student Number: %s, Section ID: %s", enlistment.studentNumber(), enlistment.sectionId()));
            }
            throw new ReferentialIntegrityViolationException(e.getMessage());
        }
    }

}
