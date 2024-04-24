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
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.KtplpGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;

import java.io.Serializable;

/**
 * Комплектная трансформаторная подстанция линейных потребителей
 *
 */

@GroupSequenceProvider(KtplpGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ktplp")
public class Ktplp  implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    Параметры КТПЛП
    @NotNull(message = "Мощность не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Сan not be 0 or less than 0", groups = OnActiveCheck.class)
    private Integer power;

    //    Количество КТПЛП, км
    @NotNull(message = "Длина не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Сan not be 0 or less than 0", groups = OnActiveCheck.class)
    private Integer count;

    //    этап строительства
    @Min(value = 1, message = "Сan not be less than 1", groups = OnActiveCheck.class)
    private Integer stage;

    //    необходимые ресурсы, чел/дней
    private Integer resource;
}
