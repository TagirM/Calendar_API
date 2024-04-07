package ru.tomsknipineft.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.oilPad.BackfillWell;

import java.util.Optional;

@Repository
public interface BackfillWellRepository extends JpaRepository<BackfillWell, Long> {
    /*
    Поиск сущности с количеством скважин соответствующий заданному или ближайший больший
     */
    Optional<BackfillWell> findFirstByWellGreaterThanEqual(Integer wellFromQuery);
}
