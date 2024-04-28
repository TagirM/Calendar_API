package ru.tomsknipineft.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.services.excelCreated.ExcelCreatedService;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalendarRestController {

    private final CalendarService calendarService;

    private final ExcelCreatedService excelCreatedService;

    private static final Logger logger = LogManager.getLogger(CalendarRestController.class);


    @GetMapping("/calendar/to_desktop")
    public ResponseEntity<Resource> uploadingCalendar(@RequestParam("codeContract") String codeContract){
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        excelCreatedService.setCalendars(calendars);
        if (calendars.size() == 0){
            throw new NoSuchCalendarException("Ошибка выгрузки календаря, проверьте корректность введенных данных");
        }
        excelCreatedService.write();

        logger.info("Скачан календарь по шифру " + codeContract + " - " + calendars);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelCreatedService.getFilename())
                .contentLength(excelCreatedService.resource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelCreatedService.resource());
    }
}
