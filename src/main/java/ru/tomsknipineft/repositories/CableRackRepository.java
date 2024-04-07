package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tomsknipineft.entities.linearObjects.CableRack;

import java.util.Optional;

public interface CableRackRepository  extends JpaRepository<CableRack, Long> {
    /**
     Поиск сущности с протяженностью, соответствующей заданной или ближайшей большей
     */
    Optional<CableRack> findFirstByLengthGreaterThanEqual(Integer length);
}
