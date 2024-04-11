package ru.tomsknipineft.services;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Класс с методами по обработке дат календарного плана
 */
@Service
public class DateService {

    /**
     * Метод, учитывающий выходные и праздничные дни. При попадании даты на выходной, производится перенос на будний день
     * @param date исходная дата
     * @return будний день
     */
    public LocalDate workDay(LocalDate date) {
        Collection<DayOfWeek> weekends = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        Collection<LocalDate> holidays = new HashSet<>(List.of(
                LocalDate.of(date.getYear(), 1, 1),
                LocalDate.of(date.getYear(), 1, 2),
                LocalDate.of(date.getYear(), 1, 3),
                LocalDate.of(date.getYear(), 1, 4),
                LocalDate.of(date.getYear(), 1, 5),
                LocalDate.of(date.getYear(), 1, 6),
                LocalDate.of(date.getYear(), 1, 7),
                LocalDate.of(date.getYear(), 1, 8),
                LocalDate.of(date.getYear(), 2, 23),
                LocalDate.of(date.getYear(), 3, 8),
                LocalDate.of(date.getYear(), 5, 1),
                LocalDate.of(date.getYear(), 5, 9),
                LocalDate.of(date.getYear(), 6, 12),
                LocalDate.of(date.getYear(), 11, 4)));

        while (weekends.contains(date.getDayOfWeek()) || holidays.contains(date)) {
            date = date.plusDays(1);
        }
        return date;
    }

    /**
     * Метод, учитывающий крайний день календаря 10е число в декабре и 20е число в остальных месяцах для актирования.
     * Так как сроки актирования этапа работ с 1 по 10 число в декабре, с 1 по 20 - в другие месяцы.
     * Если дата выходит за рамка указанных дат, то перенос даты на следующий месяц (например, если 22 марта - то на 7 апреля)
     * @param date исходная дата
     * @return валидный день для актирования
     */
    public LocalDate checkDeadlineForActivation(LocalDate date) {
        if (date.getMonth()!= Month.DECEMBER&&date.getDayOfMonth()>20){
            int deltaDays = date.getDayOfMonth()-20;
            date = workDay(date.plusDays(18-deltaDays));
        }
        else if (date.getMonth()== Month.DECEMBER&&date.getDayOfMonth()>10){
            int deltaDays = date.getDayOfMonth()-10;
            date = workDay(date.plusDays(30-deltaDays));
        }
        return date;
    }
}
