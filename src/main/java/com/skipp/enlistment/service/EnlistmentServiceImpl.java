package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.*;
import com.skipp.enlistment.domain.Enlistment;
import com.skipp.enlistment.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class EnlistmentServiceImpl implements EnlistmentService{
    private final EnlistmentDao enlistmentRepo;
    private final SectionDao sectionRepo;
    private final EnlistmentValidator enlistmentValidator;

    @Autowired
    public EnlistmentServiceImpl(EnlistmentDao enlistmentRepo, SectionDao sectionRepo, EnlistmentValidator enlistmentValidator){
        this.enlistmentRepo = enlistmentRepo;
        this.sectionRepo = sectionRepo;
        this.enlistmentValidator = enlistmentValidator;
    }
    @Override
    public Enlistment enlist(int studentNumber, String sectionId)  {
        Student student = enlistmentValidator.existingStudentValidator(studentNumber);
        Section section = enlistmentValidator.existingSectionValidator(sectionId);

        Collection<Enlistment> enlistments = enlistmentRepo.findAllEnlistedClasses(studentNumber);

        enlistments.forEach(enlistment -> {
            enlistmentValidator.duplicateEnlistmentValidator(enlistment,studentNumber,sectionId);
            Section enlistedSection = sectionRepo.findById(enlistment.sectionId());
            enlistmentValidator.sectionSubjectValidator(enlistedSection, section, sectionId, enlistment);
            enlistmentValidator.scheduleOverlapValidator(section, enlistedSection);
        });

        enlistmentValidator.sectionCapacityValidator(section);

        return enlistmentRepo.create(student, section);
    }

    @Override
    public void cancel(int studentNumber, String sectionId) {
        enlistmentValidator.existingStudentValidator(studentNumber);
        enlistmentValidator.existingSectionValidator(sectionId);

        enlistmentRepo.findAllStudentsEnlisted(sectionId);
        enlistmentRepo.findAllEnlistedClasses(studentNumber);
        enlistmentRepo.delete(studentNumber,sectionId);
    }
}
