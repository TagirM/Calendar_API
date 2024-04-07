package ru.tomsknipineft.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IncorrectDataExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CalendarIncorrectData> handlerException(Exception exception){
        CalendarIncorrectData data = new CalendarIncorrectData(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<CalendarIncorrectData> handlerException(NoSuchCalendarException exception){
        CalendarIncorrectData data = new CalendarIncorrectData(exception.getMessage(),HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CalendarIncorrectData> handlerException(NoSuchEntityException exception){
        CalendarIncorrectData data = new CalendarIncorrectData(exception.getMessage(),HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
