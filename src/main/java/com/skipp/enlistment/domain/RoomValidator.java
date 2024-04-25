package com.skipp.enlistment.domain;

import org.springframework.stereotype.Component;

@Component
public class RoomValidator {

    //name should not be blank

    public void roomNameValidator(String name){
        if(name.isBlank()){
            throw new IllegalArgumentException("name should not be blank");
        }
    }

    //capacity should be non-negative
    public void roomCapacityValidator(int capacity){
        if(capacity < 0 ){
            throw new IllegalArgumentException(String.format("capacity must be non-negative, was %s", capacity));
        }
    }
}
