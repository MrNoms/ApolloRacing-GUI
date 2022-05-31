package com.revature.apolloracing.util.custom_exceptions;

public class ObjectDoesNotExist extends RuntimeException {
    public ObjectDoesNotExist(String objectName) {
        super("No "+objectName+" exist.");
    }
}
