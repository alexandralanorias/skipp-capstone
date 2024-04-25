package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.SubjectDao;
import com.skipp.enlistment.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional

public class SubjectServiceImpl implements SubjectService {
    private final SubjectDao subjectRepo;

    @Autowired
    public SubjectServiceImpl(SubjectDao subjectRepo){
        this.subjectRepo = subjectRepo;
    }

    @Override
    public Collection<Subject> findAllSubjects() {
        return subjectRepo.findAllSubjects();
    }

    @Override
    public Subject findSubject(String subjectId) {
        return subjectRepo.findSubject(subjectId);
    }

    @Override
    public Subject create(Subject subject) {
        return subjectRepo.create(subject);
    }

    @Override
    public void delete(String subjectId) {
        subjectRepo.findSubject(subjectId);
        subjectRepo.delete(subjectId);
    }
}
