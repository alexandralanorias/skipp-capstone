package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.EnlistmentDao;
import com.skipp.enlistment.dao.SectionDao;
import com.skipp.enlistment.domain.Section;
import com.skipp.enlistment.domain.SectionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
@Service
@Transactional
public class SectionServiceImpl implements SectionService{
    private final SectionDao sectionRepo;
    private final SectionValidator sectionValidator;

    @Autowired
    public SectionServiceImpl(SectionDao sectionRepo, SectionValidator sectionValidator){
        this.sectionRepo = sectionRepo;
        this.sectionValidator = sectionValidator;
    }

    @Override
    public Section findById(String sectionId, boolean includeStudents) {
        return sectionRepo.findById(sectionId);
    }

    @Override
    public Collection<Section> findAllSections() {
        return sectionRepo.findAllSections();
    }

    @Override
    public Section create(Section section) {
        sectionValidator.alphanumericSectionIdValidator(section.getSectionId());
        sectionValidator.roomScheduleOverlapValidator(section);
        sectionValidator.facultyScheduleOverlapValidator(section);

        return sectionRepo.create(section);
    }

    @Override
    public Section update(Section section) {
        sectionValidator.roomScheduleOverlapValidator(section);;

        sectionRepo.findById(section.getSectionId());

        return sectionRepo.update(section);
    }

    @Override
    public void delete(String sectionId) {
        sectionRepo.findById(sectionId);
        sectionRepo.delete(sectionId);
    }
}
