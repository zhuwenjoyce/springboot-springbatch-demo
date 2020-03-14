package com.mybatch.mybatch1.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public String handleArrayIndexOutOfBoundsException(Exception e) {
        e.printStackTrace();
        return "found controller error";
    }
}
