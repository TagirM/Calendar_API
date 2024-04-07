package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tomsknipineft.entities.areaObjects.Vjk;

import java.util.Optional;

public interface VjkRepository extends JpaRepository<Vjk, Long> {
    /**
     Поиск сущности с площадью, соответствующей заданной или ближайшей большей
     */
    Optional<Vjk> findFirstBySquareGreaterThanEqual(Double square);
}
