package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Pipeline;
import ru.tomsknipineft.repositories.PipelineRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class PipelineService {

    private final PipelineRepository pipelineRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param pipeline Линейный трубопровод
     * @return количество необходимого ресурса
     */
    public Integer getResourcePipeline(Pipeline pipeline){
        if (pipeline.isActive()){
            return pipelineRepository.findFirstByPipelineLayingMethodAndUnitsValveAndUnitsSODAndLengthGreaterThanEqual
                    (pipeline.getPipelineLayingMethod(), pipeline.getUnitsValve(), pipeline.getUnitsSOD(), pipeline.getLength())
                    .orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров линейного трубопровода "
                            + pipeline.getUnitsValve() + pipeline.getUnitsSOD() + pipeline.getLength())).getResource();
        }
        return 0;
    }

    /**
     * Получение сущности (Линейный трубопровод) из БД
     * @return сущность (Линейный трубопровод)
     */
    public Pipeline getFirst(){
        return pipelineRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Линейный трубопровод в базе данных отсутствует"));
    }
}
