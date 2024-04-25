package com.skipp.enlistment.domain;

import com.skipp.enlistment.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SectionValidator {

    private final SectionDao sectionRepo;
    private final RoomDao roomRepo;
    private final FacultyDao facultyRepo;
    private final SubjectDao subjectRepo;


    @Autowired
    public SectionValidator(SectionDao sectionRepo, RoomDao roomRepo,
                            FacultyDao facultyRepo, SubjectDao subjectRepo){
        this.sectionRepo = sectionRepo;
        this.roomRepo = roomRepo;
        this.facultyRepo = facultyRepo;
        this.subjectRepo = subjectRepo;
    }

    //sectionId should be alphanumberic

    public void alphanumericSectionIdValidator(String sectionId){
        if(!sectionId.matches("^[a-zA-Z0-9]*$")){
            throw new IllegalArgumentException(String.format("sectionId should be alphanumeric, was %s", sectionId));
        }
    }

    //There must be no overlap in schedule of rooms

    public void roomScheduleOverlapValidator(Section section){
        Collection<Section> sections = sectionRepo.findAllSections();
        sections.forEach(currentSection ->{
            if(section.hasScheduleOverlapWith(currentSection)){
                if(section.getRoom().equals(currentSection.getRoom())){
                    throw new ScheduleConflictException(String.format("Room %s has a schedule overlap between" +
                            " new section %s with schedule %s and current section %s with schedule " +
                            "%s.", section.getRoom(), section.getSectionId(), section.getSchedule(),
                            currentSection.getSectionId(), currentSection.getSchedule()));
                }
            }
        });
    }

    //There must be no overlap in schedule of faculty
    public void facultyScheduleOverlapValidator(Section section){
        Collection<Section> sections = sectionRepo.findAllSections();
        sections.forEach(currentSection ->{
            if(section.hasScheduleOverlapWith(currentSection)){
                if (section.getFaculty().getFacultyNumber().equals(currentSection.getFaculty().getFacultyNumber())) {
                    throw new ScheduleConflictException(String.format("Faculty %s %s FN#%s has a schedule overlap between new section" +
                            " %s with schedule %s and current section %s with schedule %s.", section.getFaculty().getFirstName(),
                            section.getFaculty().getLastName(), section.getFaculty().getFacultyNumber(), section.getSectionId(), section.getSchedule(),
                            currentSection.getSectionId(), currentSection.getSchedule()));
                }
            }
        });
    }

    // Existing Subject
    public Subject existingSubjectValidator(String subjectId){
        try{
            return subjectRepo.findSubject(subjectId);
        } catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException(String.format("Subject ID: %s not found", subjectId));
        }
    }

    // Existing Room
    public Room existingRoomValidator(String roomName){
        try{
            return roomRepo.findByName(roomName);
        }catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException(String.format("Room Name: %s not found", roomName));
        }
    }

    // Existing Faculty
    public Faculty existingFacultyValidator(int facultyNumber){
        try{
            return facultyRepo.findByNumber(facultyNumber);
        }catch (EmptyResultDataAccessException e) {
            throw new ReferentialIntegrityViolationException(String.format("Faculty Number: %s not found", facultyNumber));
        }
    }

}
