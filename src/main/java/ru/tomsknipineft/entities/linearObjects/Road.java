package ru.tomsknipineft.entities.linearObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveBridgeRoad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.RoadGroupSequenceProvider;

/**
 * Автомобильная дорога
 */
@GroupSequenceProvider(RoadGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roads")
public class Road implements OilPad, EntityProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    есть ли мост у дороги
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

    //    необходимые ресурсы, чел/дней
    private Integer resource;

    public Road(Integer category, Double length, Integer resource) {
        this.category = category;
        this.length = length;
        this.resource = resource;
    }

    // расчет необходимых ресурсов для проектирования дорожного моста
    public Integer getResourceBridge(){
        return (bridgeRoadLength/20)*bridgeRoadCount;
    }
}
