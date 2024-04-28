package ru.tomsknipineft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.repositories.CalendarRepository;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.services.DataFormProjectService;
import ru.tomsknipineft.services.DateService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BackfillWellGroupCalendarServiceImplTest {

  @Mock
  private CalendarRepository calendarRepository;

  @Mock
  private DataFormProjectService dataFormProjectService;


  @Mock
  private DateService dateService;

  private CalendarService calendarService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    calendarService = new CalendarService(calendarRepository, dateService, dataFormProjectService);
  }

  @Test
  void testGetCalendarByCode() {
    String code = "ABC123";
    when(calendarRepository.findCalendarByCodeContract(code)).thenReturn(Optional.of(List.of(new Calendar())));

    List<Calendar> result = calendarService.getCalendarByCode(code);

    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testCreateCalendar() {
    List<Integer> durations = List.of(10, 20, 30);
    String code = "DEF456";
    LocalDate start = LocalDate.now();
    Integer humanFactor = 10;
    boolean fieldEngineeringSurvey = true;
    boolean engineeringSurveyReport = true;
    Integer drillingRig = 5;
    when(dataFormProjectService.getFilePathSave()).thenReturn("dataFormProjectSave/recover.ser");
    assertDoesNotThrow(() -> {
      calendarService.createCalendar(durations, code, start, humanFactor, fieldEngineeringSurvey,
              engineeringSurveyReport, drillingRig, new DataFormOilPad());
    });
  }

}