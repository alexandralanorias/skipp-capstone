package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.service.SubjectService;
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
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final AppUserValidator appUserValidator;

    // TODO What bean should be wired here?
    @Autowired

    public SubjectController(SubjectService subjectService, AppUserValidator appUserValidator) {
        this.subjectService = subjectService;
        this.appUserValidator = appUserValidator;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Subject> getSubjects() {
        // TODO implement this handler
        return subjectService.findAllSubjects();
    }

    // TODO What @XXXMapping annotation should be put here?
    @PostMapping
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.CREATED)
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public Subject createSubject(@RequestBody Subject subject, Authentication auth) {
        // TODO implement this handler
        Subject newSubject;

        appUserValidator.facultyRoleValidator(auth);

        try {
            newSubject = subjectService.create(subject);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException("Subject with subjectId " + subject.getSubjectId() + " already exists.");
        }

        return newSubject;
    }

    // TODO What @XXXMapping annotation should be put here?
    @DeleteMapping("/{subjectId}")
    @ResponseStatus(HttpStatus.OK)
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public void deleteSubject(@PathVariable String subjectId, Authentication auth) {
        // TODO implement this handler
        appUserValidator.facultyRoleValidator(auth);

        try {
            subjectService.delete(subjectId);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Subject SubjectId: %s not found", subjectId));
            }

            throw new ReferentialIntegrityViolationException("Subject " + subjectId + " is still being used.");
        }
    }
}
