package ru.tomsknipineft.entities.oilPad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;

import java.io.Serializable;

/**
 * Инженерная подготовка кустовой площадки
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "backfill_wells")
public class BackfillWell implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active = true;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    количество скважин
    @NotNull(message = "The well should not be empty")
    @Min(value = 1, message = "Сan not be less than 1")
    @Max(value = 24, message = "Сan not be more than 24")
    private Integer well;

    //    этап строительства
    @Min(value = 1, message = "Сan not be less than 1")
    private Integer stage;

    //    необходимые ресурсы для выполнения полевых ИИ, чел/дней
    @Column(name = "resource_for_eng_survey")
    private Integer resourceForEngSurvey;

    //    необходимые ресурсы для выполнения ЛИ, чел/дней
    @Column(name = "resource_for_lab_research")
    private Integer resourceForLabResearch;

    //    необходимые ресурсы для выполнения отчета ИИ, чел/дней
    @Column(name = "resource_for_eng_survey_report")
    private Integer resourceForEngSurveyReport;

    //    необходимые ресурсы для разработки РД, чел/дней
    @Column(name = "resource_for_work_doc")
    private Integer resourceForWorkDoc;

    //    необходимые ресурсы для разработки ПД, чел/дней
    @Column(name = "resource_for_proj_doc")
    private Integer resourceForProjDoc;

    //    необходимые ресурсы для разработки СД, чел/дней
    @Column(name = "resource_for_est_doc")
    private Integer resourceForEstDoc;
}
