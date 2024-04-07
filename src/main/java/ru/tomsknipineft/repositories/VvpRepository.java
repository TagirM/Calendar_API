package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tomsknipineft.entities.areaObjects.Vvp;

import java.util.Optional;

public interface VvpRepository  extends JpaRepository<Vvp, Long> {
    /*
    Поиск сущности с площадью соответствующей заданной или ближайшей большей
     */
    Optional<Vvp> findFirstBySquareGreaterThanEqualAndHelicopterModel(Double square, String helicopterModel);
}
