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
import ru.tomsknipineft.entities.enumEntities.PipelineLayingMethod;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.PipelineGroupSequenceProvider;

import java.io.Serializable;

/**
 * Линейный трубопровод
 */
@GroupSequenceProvider(PipelineGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pipeline")
public class Pipeline  implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    // тип объекта проектирования
    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

//    способ прокладки трубопровода
    @NotNull(message = "Способ прокладки не указан", groups = OnActiveCheck.class)
    @Column(name = "pipeline_laying_method")
    @Enumerated(EnumType.STRING)
    private PipelineLayingMethod pipelineLayingMethod;

    //    протяженность трубпровода, км
    @NotNull(message = "Длина не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Длина не может быть 0 или отрицательной", groups = OnActiveCheck.class)
    private Double length;

//    сложность геологии
    @NotNull(message = "Сложность прокладки не указана", groups = OnActiveCheck.class)
    @Column(name = "complexity_of_geology")
    @Enumerated(EnumType.STRING)
    private ComplexityOfGeology complexityOfGeology;

    // Количество узлов запорной арматуры
    @Min(value = 0, message = "Не может быть меньше 0", groups = OnActiveCheck.class)
    private Integer unitsValve;

    // Количество узлов средств очистки и диагностики (СОД)
    @Min(value = 0, message = "Не может быть меньше 0", groups = OnActiveCheck.class)
    private Integer unitsSOD;

    //    этап строительства
    @Min(value = 1, message = "Не может быть меньше 1", groups = OnActiveCheck.class)
    private Integer stage;

    //    необходимые ресурсы, чел/дней
    private Integer resource;
}
