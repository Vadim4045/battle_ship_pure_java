package com.gmail.focusdigit;

public class CustomException extends RuntimeException {
    public CustomException(String message){
        System.out.println(message);
    }
}
