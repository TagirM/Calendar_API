package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.oilPad.Mupn;

import java.util.Optional;

@Repository
public interface MupnRepository extends JpaRepository<Mupn, Long> {
    /*
    Поиск сущности с площадью соответствующей заданной или ближайшей большей
     */
    Optional<Mupn> findFirstBySquareGreaterThanEqual(Double square);
}
