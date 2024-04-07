package ru.tomsknipineft.entities.oilPad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.ObjectType;

/**
 * Инженерная подготовка кустовой площадки
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "backfill_wells")
public class BackfillWell implements OilPad, EntityProject {

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

    //    необходимые ресурсы, чел/дней
    private Integer resource;
}
