package ru.tomsknipineft.entities.areaObjects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;

/**
 * Инженерная подготовка любого площадочного объекта
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "backfill_sites")
public class BackfillSite implements OilPad, EntityProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    площадь отсыпки, га
    private Double square;

//    этап строительства
    private Integer stage;

    //    необходимые ресурсы, чел/дней
    private Integer resource;
}
