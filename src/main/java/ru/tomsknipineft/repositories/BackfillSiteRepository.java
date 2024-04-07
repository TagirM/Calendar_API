package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;

import java.util.Optional;

@Repository
public interface BackfillSiteRepository extends JpaRepository<BackfillSite, Long> {
    /*
    Поиск сущности с площадью соответствующей заданной или ближайшей большей
     */
    Optional<BackfillSite> findFirstBySquareGreaterThanEqual(Double squareFromQuery);
}
