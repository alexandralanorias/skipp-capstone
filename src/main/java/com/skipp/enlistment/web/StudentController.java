package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.dto.StudentDto;
import com.skipp.enlistment.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;
    private final AppUserValidator appUserValidator;
    // TODO What bean should be wired here?
    @Autowired

    public StudentController(StudentService studentService, AppUserValidator appUserValidator){
        this.studentService = studentService;
        this.appUserValidator = appUserValidator;
    }
    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Student> getStudents() {
        // TODO implement this handler
        return studentService.findAllStudents();
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping("/{studentNumber}")
    @ResponseStatus(HttpStatus.OK)
    // Hint: The method argument should give you an idea how it would look like.
    public StudentDto getStudent(@PathVariable Integer studentNumber, Authentication auth) {
        // TODO implement this handler
        // Hint: 'auth' is where you can get the username of the user accessing the API
        Student student;
        appUserValidator.facultyRoleValidator(auth);

        try{
            student = studentService.findByNumber(studentNumber, true);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Student Number: %s not found", studentNumber));
        }

        return new StudentDto(student, true);
    }

    // TODO What @XXXMapping annotation should be put here?
    @PostMapping
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.CREATED)
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public Student createStudent(@RequestBody Student student, Authentication auth) {
        // TODO implement this handler
        Student newStudent;
        appUserValidator.facultyRoleValidator(auth);

        try{
            newStudent = studentService.create(student);
        } catch (DuplicateKeyException e) {
            throw new RecordAlreadyExistsException("Student with studentNumber " + student.getStudentNumber() + " already exists.");
        }

        return newStudent;
    }

    // TODO What @XXXMapping annotation should be put here?
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public Student updateStudent(@RequestBody Student student, Authentication auth) {
        // TODO implement this handler
        Student updatedStudent;
        appUserValidator.facultyRoleValidator(auth);

        try{
            updatedStudent = studentService.update(student);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Student Number: %s not found", student.getStudentNumber()));
        }

        return updatedStudent;
    }

    // TODO What @XXXMapping annotation should be put here?
    @DeleteMapping("/{studentNumber}")
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public void deleteStudent(@PathVariable Integer studentNumber, Authentication auth) {
        // TODO implement this handler
        appUserValidator.facultyRoleValidator(auth);
        try {
            studentService.delete(studentNumber);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {

            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException(String.format("Student Number: %s not found", studentNumber));
            }

            throw new ReferentialIntegrityViolationException("Student " + studentNumber + " is still enrolled in a section.");
        }
    }
}
