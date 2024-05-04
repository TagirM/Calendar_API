package ru.tomsknipineft.entities.linearObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ComplexityOfGeology;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.LineGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;

import java.io.Serializable;

/**
 * ЛЭП
 */
@GroupSequenceProvider(LineGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lines")
public class Line implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    мощность ВЛ (или габариты ВЛ)
    @NotNull(message = "Мощность не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Сan not be 0 or less than 0", groups = OnActiveCheck.class)
    private Integer power;

    //    протяженность ЛЭП, км
    @NotNull(message = "Длина не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Сan not be 0 or less than 0", groups = OnActiveCheck.class)
    private Double length;

    //    сложность геологии
    @NotNull(message = "Сложность геологии не указана", groups = OnActiveCheck.class)
    @Column(name = "complexity_of_geology")
    @Enumerated(EnumType.STRING)
    private ComplexityOfGeology complexityOfGeology;

    //    этап строительства
    @Min(value = 1, message = "Сan not be less than 1", groups = OnActiveCheck.class)
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
