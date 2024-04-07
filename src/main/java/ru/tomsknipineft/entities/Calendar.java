package ru.tomsknipineft.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Календарный план со сроками проектирования всех стадий договора (по этапу строительства)
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    шифр договора
    @NotNull(message = "The code should not be empty")
    @Size(min = 4, max = 10, message = "Code should be between 4 and 10 characters")
    private String codeContract;

    //    начало выполнения работ по договору
    @NotNull(message = "The date should not be empty")
    @FutureOrPresent(message = "The date can not be past")
    private LocalDate startContract;

    //    этап строительства
    private Integer stage;

    //    дата окончания полевых ИИ (инженерных изысканий)
    private LocalDate engineeringSurvey;
    //    дата выдачи отчета ИИ
    private LocalDate engineeringSurveyReport;
    //    дата согласования отчета ИИ
    private LocalDate agreementEngineeringSurvey;

    //    дата начала разработки РД (рабочей документации)
    private LocalDate workingStart;
    //    дата выдачи РД (рабочей документации)
    private LocalDate workingFinish;
    //    дата согласования РД
    private LocalDate agreementWorking;

    //    дата выдачи СД (сметной документации)
    private LocalDate estimatesFinish;
    //    дата согласования СД
    private LocalDate agreementEstimates;

    //    дата выдачи ПД (проектной документации)
    private LocalDate projectFinish;
    //    дата согласования ПД
    private LocalDate agreementProject;
    //    дата заключения эспертизы ПД
    private LocalDate examination;

    //    дата выдачи ППиМТ
    private LocalDate landFinish;

    //    дата создания календарного плана
    private LocalDateTime dateOfCreated;

    // человеческий фактор, %
    private Integer humanFactor;

    // сохранение в виде файла данных о проекте
    @Lob
    @Column(name = "bytes_data_project", columnDefinition = "LONGBLOB")
    private byte[] bytesDataProject;

    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }

}
