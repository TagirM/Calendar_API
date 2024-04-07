package ru.tomsknipineft.entities.areaObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.VvpGroupSequenceProvider;

/**
 * Временная вертолетная площадка (ВВП)
 */
@GroupSequenceProvider(VvpGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vvps")
public class Vvp implements OilPad, EntityProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    для посадки какого вертолета предназначена ВВП
    @NotNull(message = "Информация по вертолету не заполнена", groups = OnActiveCheck.class)
    @Size(min = 3, max = 10, message = "наименование модели находится в интервале 3-10 символов", groups = OnActiveCheck.class)
    private String helicopterModel;

    //    наличие зала ожидания
    @Column(name = "hall_exist")
    private boolean hallExist;

    //    площадь отсыпки, га
    @NotNull(message = "Площадь не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Площадь не может быть отрицательной", groups = OnActiveCheck.class)
    private Double square;

    //    этап строительства
    @Min(value = 1, message = "Не может быть меньше 1", groups = OnActiveCheck.class)
    private Integer stage;

    //    необходимые ресурсы, чел/дней
    private Integer resource;
}
