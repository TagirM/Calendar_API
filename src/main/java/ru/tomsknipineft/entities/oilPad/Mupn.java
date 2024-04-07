package ru.tomsknipineft.entities.oilPad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.ObjectType;
import ru.tomsknipineft.utils.entityValidator.MupnGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;

/**
 * Площадка МУПН (мобильная установка подготовки нефти)
 */
@GroupSequenceProvider(MupnGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mupns")
public class Mupn implements OilPad, EntityProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

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
