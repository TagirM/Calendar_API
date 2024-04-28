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
import ru.tomsknipineft.entities.enumEntities.MpsType;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.MpsGroupSequenceProvider;

import java.io.Serializable;

/**
 * Модульная трансформаторная подстанция
 */
@GroupSequenceProvider(MpsGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mps")
public class Mps implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    // тип подстанции, МВт
    @NotNull(message = "Тип подстанции не выбран", groups = OnActiveCheck.class)
    @Column(name = "mps_type")
    @Enumerated(EnumType.STRING)
    private MpsType mpsType;

    // предусмотрена ли отсыпка для МПС в данном проекте
    @NotNull(message = "Площадь не заполнена", groups = OnActiveCheck.class)
    @Column(name = "backfill_site")
    private boolean backfillSite;

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
