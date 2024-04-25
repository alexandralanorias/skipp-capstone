package com.skipp.enlistment.service;

import com.skipp.enlistment.dao.*;
import com.skipp.enlistment.domain.*;
import com.skipp.enlistment.domain.StudentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentRepo;
    private final EnlistmentDao enlistmentRepo;
    private final SectionDao sectionRepo;
    private final AppUserDao appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final StudentValidator studentValidator;

    @Autowired
    public StudentServiceImpl(StudentDao studentRepo, EnlistmentDao enlistmentRepo,
                              SectionDao sectionRepo, AppUserDao appUserRepo,
                              PasswordEncoder passwordEncoder, StudentValidator studentValidator) {
        this.studentRepo = studentRepo;
        this.enlistmentRepo = enlistmentRepo;
        this.sectionRepo = sectionRepo;
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.studentValidator = studentValidator;
    }

    @Override
    public Collection<Student> findAllStudents() {
        return studentRepo.findAllStudents();
    }

    @Override
    public Student findByNumber(int studentNumber, boolean includeSections) {
        Student student = studentRepo.findByNumber(studentNumber);

        if(includeSections) {
            Collection<Enlistment> enlistments = enlistmentRepo.findAllEnlistedClasses(studentNumber);
            enlistments.forEach(enlistment -> {
                Section section = sectionRepo.findById(enlistment.sectionId());
                student.addSection(section);
            });
        }

        return student;
    }

    @Override
    public Student create(Student student) {
        Student newStudent = studentRepo.create(student);
        studentValidator.studentNumberValidator(student.getStudentNumber());
        studentValidator.studentFNameValidator(student.getFirstName());
        studentValidator.studentLNameValidator(student.getLastName());

        AppUser appUser = studentAppUser(newStudent);
        appUserRepo.create(appUser);

        return newStudent;
    }

    @Override
    public Student update(Student student) {
        studentValidator.studentFNameValidator(student.getFirstName());
        studentValidator.studentLNameValidator(student.getLastName());

        studentRepo.findByNumber(student.getStudentNumber());

        Student updatedStudent = studentRepo.update(student);
        AppUser appUser = studentAppUser(updatedStudent);
        appUserRepo.update(appUser);

        return updatedStudent;
    }

    @Override
    public void delete(int studentNumber) {
        studentRepo.findByNumber(studentNumber);

        studentRepo.delete(studentNumber);
    }

    private AppUser studentAppUser(Student student) {
        String password = passwordEncoder.encode(student.getFirstName().replace(" ", "")+student.getLastName().replace(" ", ""));
        return new AppUser(String.format("ST-%s", student.getStudentNumber()), password, "STUDENT");
    }
}
