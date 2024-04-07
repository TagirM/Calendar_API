package ru.tomsknipineft.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarIncorrectData {
    private String messageException;
    private Integer statusCode;
}
