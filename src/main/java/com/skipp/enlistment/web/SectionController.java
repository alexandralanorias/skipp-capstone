package com.skipp.enlistment.web;

import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.dto.SectionDto;
import com.skipp.enlistment.service.FacultyService;
import com.skipp.enlistment.service.RoomService;
import com.skipp.enlistment.service.SectionService;
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
@RequestMapping("/sections")
public class SectionController {

    private final SectionService sectionService;
    private final SubjectService subjectService;
    private final RoomService roomService;
    private final FacultyService facultyService;
    private final AppUserValidator appUserValidator;
    private final SectionValidator sectionValidator;



    // TODO What bean/s should be wired here?
    @Autowired

    public SectionController(SectionService sectionService, SubjectService subjectService,
                             RoomService roomService, FacultyService facultyService,
                             AppUserValidator appUserValidator, SectionValidator sectionValidator){
        this.sectionService = sectionService;
        this.subjectService = subjectService;
        this.roomService = roomService;
        this.facultyService = facultyService;
        this.appUserValidator = appUserValidator;
        this.sectionValidator = sectionValidator;
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<SectionDto> getAllSections() {
        // TODO implement this handler
        return sectionService.findAllSections().stream().map((Section section) ->
                new SectionDto(section, false)).toList();
    }

    // TODO What @XXXMapping annotation should be put here?
    @GetMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    // Hint: The method argument should give you an idea how it would look like.
    public SectionDto getSection(@PathVariable String sectionId) {
        // TODO implement this handler
        Section section;
        try{
            section = sectionService.findById(sectionId,true);
        }catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException(String.format("Section ID: %s not found", sectionId));
        }

        return new SectionDto(section, true);
    }

    // TODO What @XXXMapping annotation should be put here?
    @PostMapping
    // TODO What's the annotation to use to define the status code returned when a handler is successful?
    // TODO What's the appropriate status code for this handler?
    @ResponseStatus(HttpStatus.CREATED)
    // Hint: What does the test expect?
    // TODO This should only be accessed by faculty. Apply the appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public SectionDto createSection(@RequestBody SectionDto section, Authentication auth) {
        // TODO implement this handler
        Section newSection;

        Subject subject = sectionValidator.existingSubjectValidator(section.getSubjectId());
        Room room = sectionValidator.existingRoomValidator(section.getRoomName());
        Faculty faculty = sectionValidator.existingFacultyValidator(section.getFacultyNumber());
        Schedule schedule = Schedule.valueOf(section.getSchedule());

        appUserValidator.facultyRoleValidator(auth);

        Section sectionDetails = new Section(section.getSectionId(), subject, schedule, room, faculty);

        try{
            newSection = sectionService.create(sectionDetails);
        }catch (DuplicateKeyException | ScheduleConflictException e) {

            if (e instanceof ScheduleConflictException) {
                throw new SectionCreationException(e.getMessage());
            }

            throw new RecordAlreadyExistsException("Section " + section.getSectionId() + " already exists.");

        }

        return new SectionDto(newSection, true);
    }

    // TODO What @XXXMapping annotation should be put here?
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public SectionDto updateSection(@RequestBody SectionDto section, Authentication auth) {
        // TODO implement this handler
        Section updatedSection;

        Subject subject = sectionValidator.existingSubjectValidator(section.getSubjectId());
        Room room = sectionValidator.existingRoomValidator(section.getRoomName());
        Faculty faculty = sectionValidator.existingFacultyValidator(section.getFacultyNumber());
        Schedule schedule = Schedule.valueOf(section.getSchedule());

        appUserValidator.facultyRoleValidator(auth);

        Section sectionDetails = new Section(section.getSectionId(), subject, schedule, room, faculty);

        try{
            updatedSection = sectionService.update(sectionDetails);
        }catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException("Section ID: " + section.getSectionId() + " not found");
        }

        return new SectionDto(updatedSection, true);
    }

    // TODO What @XXXMapping annotation should be put here?
    @DeleteMapping("/{sectionId}")
    // Hint: The method argument should give you an idea how it would look like.
    // TODO This should only be accessed by faculty. Apply appropriate annotation.
    @PreAuthorize("hasRole('FACULTY')")
    public void deleteSection(@PathVariable String sectionId, Authentication auth) {
        appUserValidator.facultyRoleValidator(auth);
        try{
            sectionService.delete(sectionId);
        } catch (DataIntegrityViolationException | EmptyResultDataAccessException e) {
            if (e instanceof EmptyResultDataAccessException) {
                throw new RecordNotFoundException("Section ID: " + sectionId + " not found");
            }

            throw new ReferentialIntegrityViolationException("Section ID: " + sectionId + " is being referenced by other entities");
        }
    }

}
