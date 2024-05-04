package ru.tomsknipineft.entities.linearObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveBridgeRoad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.RoadGroupSequenceProvider;

import java.io.Serializable;

/**
 * Автомобильная дорога
 */
@GroupSequenceProvider(RoadGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roads")
public class Road implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    // наличие мостов у дороги
    @Column(name = "bridge_exist")
    private boolean bridgeExist;

    // количество мостов
    @Column(name = "bridge_road_count")
    @NotNull(message = "Количество не заполнено", groups = OnActiveBridgeRoad.class)
    @Positive(message = "Количество не может быть 0 или отрицательным", groups = OnActiveBridgeRoad.class)
    private Integer bridgeRoadCount;

    // общая длина мостов, м
    @Column(name = "bridge_road_length")
    @NotNull(message = "Длина моста не заполнена", groups = OnActiveBridgeRoad.class)
    @Positive(message = "Длина моста не может быть 0 или отрицательной", groups = OnActiveBridgeRoad.class)
    private Integer bridgeRoadLength;

    //    категория дороги
    @NotNull(message = "Категория не заполнена", groups = OnActiveCheck.class)
    @Min(value = 3, message = "Неверно указана категория", groups = OnActiveCheck.class)
    @Max(value = 4, message = "Неверно указана категория", groups = OnActiveCheck.class)
    private Integer category;

    //    протяженность дороги, км
    @NotNull(message = "Длина не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Длина не может быть 0 или отрицательной", groups = OnActiveCheck.class)
    private Double length;

    //    этап строительства
    @Min(value = 1, message = "Не может быть меньше 1", groups = OnActiveCheck.class)
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
    public Road(Integer category, Double length, Integer resourceForWorkDoc) {
        this.category = category;
        this.length = length;
        this.resourceForWorkDoc = resourceForWorkDoc;
    }

    // расчет необходимых ресурсов для проектирования дорожного моста
    public Integer getResourceBridge(){
        return (bridgeRoadLength/20)*bridgeRoadCount;
    }
}
