package com.skipp.enlistment.domain;

import com.skipp.enlistment.dao.AppUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AppUserValidator {
    private final AppUserDao appUserRepo;

    @Autowired
    public AppUserValidator(AppUserDao appUserRepo){
        this.appUserRepo = appUserRepo;
    }

    public void studentRoleValidator(Authentication auth){
        AppUser appUser = appUserRepo.findByUsername(auth.getName());
        if(!appUser.getRole().equals("STUDENT")){
            throw new AccessDeniedException("Access Denied");
        }
    }

    public void facultyRoleValidator(Authentication auth){
        AppUser appUser = appUserRepo.findByUsername(auth.getName());
        if(!appUser.getRole().equals("FACULTY")){
            throw new AccessDeniedException("Access Denied");
        }
    }

    // The student can only enlist himself/herself; he/she cannot enroll another student.
    public void studentAuthorization(Authentication auth, Integer studentNumber){
        String authenticationID = auth.getName();
        if(!authenticationID.equals("ST-"+studentNumber)){
            throw new AccessDeniedException("You cannot enlist for another student");
        }
    }
}
