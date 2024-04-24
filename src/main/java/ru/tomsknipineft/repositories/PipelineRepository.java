package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.enumEntities.PipelineLayingMethod;
import ru.tomsknipineft.entities.linearObjects.Pipeline;

import java.util.Optional;

@Repository
public interface PipelineRepository  extends JpaRepository<Pipeline, Long> {
    Optional<Pipeline> findByPipelineLayingMethodAndLength(PipelineLayingMethod pipelineLayingMethod, Double length);
    /**
     Поиск сущности со способом прокладки, узлами и протяженностью, соответствующих заданным или ближайшими большими
     */
    Optional<Pipeline> findFirstByPipelineLayingMethodAndUnitsValveAndUnitsSODAndLengthGreaterThanEqual
    (PipelineLayingMethod pipelineLayingMethod, Integer unitsValve, Integer unitsSOD, Double length);

}
