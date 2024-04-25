package com.skipp.enlistment.domain;
import org.springframework.stereotype.Component;

@Component
public class FacultyValidator {

    // facultyNumber must be non-negative
    public void facultyNumberValidator(int facultyNumber){
        if (facultyNumber < 0){
            throw new IllegalArgumentException("facultyNumber must be non-negative, was "+facultyNumber);
        }
    }

    // firstName should not be blank
    public void facultyFNameValidator (String firstName){
        if(firstName.isBlank()){
            throw new IllegalArgumentException("firstName should not be blank");
        }
    }

    // lastName should not be blank
    public void facultyLNameValidator (String lastName){
        if(lastName.isBlank()){
            throw new IllegalArgumentException("lastName should not be blank");
        }
    }
}
