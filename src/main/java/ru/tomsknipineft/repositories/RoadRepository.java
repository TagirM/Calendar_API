package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.linearObjects.Road;

import java.util.Optional;

@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {
    Optional<Road> findByCategoryAndLength(Integer category, Double length);
    /*
    Поиск сущности с категорией и протяженностью соответствующих заданным или ближайшими большими
     */
    Optional<Road> findFirstByCategoryAndLengthGreaterThanEqual(Integer category, Double length);
}
