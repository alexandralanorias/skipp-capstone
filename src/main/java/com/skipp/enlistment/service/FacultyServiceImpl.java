package com.skipp.enlistment.service;

import com.skipp.enlistment.domain.AppUser;
import com.skipp.enlistment.domain.Faculty;
import com.skipp.enlistment.dao.*;
import com.skipp.enlistment.domain.FacultyValidator;
import com.skipp.enlistment.domain.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {
    private final FacultyDao facultyRepo;
    private final SectionDao sectionRepo;
    private final AppUserDao appUserRepo;
    private final PasswordEncoder passwordEncoder;
    private final FacultyValidator facultyValidator;

    @Autowired
    public FacultyServiceImpl(FacultyDao facultyRepo, SectionDao sectionRepo,
                              AppUserDao appUserRepo, PasswordEncoder passwordEncoder,
                              FacultyValidator facultyValidator) {
        this.facultyRepo = facultyRepo;
        this.sectionRepo = sectionRepo;
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.facultyValidator = facultyValidator;
    }

    @Override
    public Collection<Faculty> findAllFaculty() {
        return facultyRepo.findAllFaculty();
    }

    @Override
    public Faculty findByNumber(int facultyNumber, boolean includeSections) {
        Faculty faculty = facultyRepo.findByNumber(facultyNumber);

        if(includeSections){
            Collection<Section> sections = sectionRepo.findByFaculty(facultyNumber);
            sections.forEach(section -> faculty.addSection(section));
        }

        return faculty;
    }

    @Override
    public Faculty create(Faculty faculty) {
        facultyValidator.facultyNumberValidator(faculty.getFacultyNumber());
        facultyValidator.facultyFNameValidator(faculty.getFirstName());
        facultyValidator.facultyLNameValidator(faculty.getLastName());

        Faculty newFaculty = facultyRepo.create(faculty);
        AppUser appUser = facultyAppUser(faculty);
        appUserRepo.create(appUser);
        return newFaculty;
    }

    @Override
    public Faculty update(Faculty faculty) {
        facultyValidator.facultyFNameValidator(faculty.getFirstName());
        facultyValidator.facultyLNameValidator(faculty.getLastName());

        facultyRepo.findByNumber(faculty.getFacultyNumber());
        AppUser appUser = facultyAppUser(faculty);
        Faculty updatedFaculty = facultyRepo.update(faculty);
        appUserRepo.update(appUser);
        return updatedFaculty;
    }

    @Override
    public void delete(int facultyNumber) {
        facultyRepo.findByNumber(facultyNumber);
        facultyRepo.delete(facultyNumber);
    }

    private AppUser facultyAppUser(Faculty faculty) {
        String password = passwordEncoder.encode(faculty.getFirstName().replace(" ", "")+faculty.getLastName().replace(" ", ""));
        return new AppUser(String.format("FC-%s", faculty.getFacultyNumber()), password, "FACULTY");
    }
}
