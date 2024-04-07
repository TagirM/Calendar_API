package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.Calendar;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    /*
    Поиск календарного плана по шифру договора
     */
    Optional<List<Calendar>> findCalendarByCodeContract(String codeContract);

    /*
    Поиск календарного плана по шифру договора и этапу строительства
     */
    Optional<List<Calendar>> findCalendarByCodeContractAndStage(String codeContract, Integer stage);

}
