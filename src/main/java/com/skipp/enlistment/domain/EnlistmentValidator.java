package com.skipp.enlistment.domain;

import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Collection;
@Component
public class EnlistmentValidator {

    private final EnlistmentDao enlistmentRepo;
    private final StudentDao studentRepo;
    private final SectionDao sectionRepo;

    @Autowired
    public EnlistmentValidator(EnlistmentDao enlistmentRepo, StudentDao studentRepo, SectionDao sectionRepo){
        this.enlistmentRepo=enlistmentRepo;
        this.studentRepo=studentRepo;
        this.sectionRepo=sectionRepo;

    }

    // Student with studentNumber should exist
    public Student existingStudentValidator(int studentNumber){
        try{
            return studentRepo.findByNumber(studentNumber);
        }catch(EmptyResultDataAccessException e){
            throw new RecordNotFoundException(String.format("Student with number %s not found", studentNumber));
        }
    }

    // Section with sectionId should exist
    public Section existingSectionValidator(String sectionId){
        try{
            return sectionRepo.findById(sectionId);
        }catch(EmptyResultDataAccessException e){
            throw new RecordNotFoundException(String.format("Section with id %s not found", sectionId));
        }
    }

    // The student should not be enrolled in the same class more than once.
    public void duplicateEnlistmentValidator(Enlistment enlistment, int studentNumber, String sectionId){
        if(enlistment.studentNumber()==(studentNumber) && enlistment.sectionId().equals(sectionId)){
            throw new DuplicateEnlistmentException(String.format("Enlisted more than once: %s", sectionId));
        }
    }

    // The student should not enlist in more than one section with the same subject.
    public void sectionSubjectValidator(Section section, Section enlistedSection, String sectionId, Enlistment enlistment){
        if(section.getSubject().getSubjectId().equals(enlistedSection.getSubject().getSubjectId())){
            throw new SameSubjectException(String.format("Section %s with subject %s has same subject as currently enlisted section %s", sectionId, section.getSubject().getSubjectId(), enlistment.sectionId()));
        }
    }

    // There must be no conflict in schedule between this section and the other sections that the student is already enlisted in.
    public void scheduleOverlapValidator(Section section, Section enlistedSection){
        enlistedSection.getSchedule().notOverlappingWith(section.getSchedule());
    }

    // The number of students enlisted in a section must not exceed the capacity of the room assigned to it.
    public void sectionCapacityValidator(Section section){
        Collection<Enlistment> enlistments = enlistmentRepo.findAllStudentsEnlisted(section.getSectionId());
        if(enlistments.size() >= section.getRoom().getCapacity()){
            throw new RoomCapacityReachedException(String.format("Capacity Reached - enlistments: %s; capacity: %s",enlistments.size(),section.getRoom().getCapacity()));
        }
    }
}
