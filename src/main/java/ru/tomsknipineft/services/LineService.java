package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.repositories.LineRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class LineService implements EntityProjectService{

    private final LineRepository lineRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param line ЛЭП
     * @return количество необходимого ресурса
     */
    public Integer getResourceLine(Line line){
        if (line.isActive()){
            return lineRepository.findFirstByPowerAndLengthGreaterThanEqual(line.getPower(), line.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введены некорректные значения параметров ВЛ " + line.getPower() + " и " + line.getLength())).getResourceForWorkDoc();
        }

        return 0;
    }

    /**
     * Получение сущности (ЛЭП) из БД
     * @return сущность (ЛЭП)
     */
    public Line getFirst(){
        return lineRepository.findById(1L).orElseThrow();
    }
}
