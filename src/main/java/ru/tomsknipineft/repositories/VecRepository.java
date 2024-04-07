package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.areaObjects.Vec;

import java.util.Optional;

@Repository
public interface VecRepository extends JpaRepository<Vec, Long> {
    /**
    Поиск сущности с мощностью и площадью, соответствующих заданным или ближайшими большими
     */
    Optional<Vec> findFirstByPowerAndSquareGreaterThanEqual(Integer power, Double square);
}
