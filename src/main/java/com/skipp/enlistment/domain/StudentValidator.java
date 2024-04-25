package com.skipp.enlistment.domain;

import org.springframework.stereotype.Component;

@Component
public class StudentValidator {
    // studentNumber must be non-negative
    public void studentNumberValidator(int facultyNumber){
        if (facultyNumber < 0){
            throw new IllegalArgumentException(String.format("facultyNumber must be non-negative, was %s", facultyNumber));
        }
    }

    // firstName should not be blank
    public void studentFNameValidator (String firstName){
        if(firstName.isBlank()){
            throw new IllegalArgumentException("firstName should not be blank");
        }
    }

    // lastName should not be blank
    public void studentLNameValidator (String lastName){
        if(lastName.isBlank()){
            throw new IllegalArgumentException("lastName should not be blank");
        }
    }
}
