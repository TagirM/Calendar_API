package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.linearObjects.Line;


import java.util.Optional;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {
    Optional<Line> findByPowerAndLength(Integer power, Double length);
    /**
    Поиск сущности с мощностью и протяженностью, соответствующих заданным или ближайшими большими
     */
    Optional<Line> findFirstByPowerAndLengthGreaterThanEqual(Integer power, Double length);

}
