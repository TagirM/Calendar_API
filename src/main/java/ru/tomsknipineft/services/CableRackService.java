package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.repositories.CableRackRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class CableRackService implements EntityProjectService{

    private final CableRackRepository cableRackRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param cableRack Кабельная эстакада
     * @return количество необходимого ресурса
     */
    public Integer getResourceLine(CableRack cableRack){
        if (cableRack.isActive()){
            return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRack.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введены некорректные значения параметров кабельной эстакады " + cableRack.getLength())).getResource();
        }

        return 0;
    }

    /**
     * Получение сущности (Кабельная эстакада) из БД
     * @return сущность (Кабельная эстакада)
     */
    public CableRack getFirst(){
        return cableRackRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Кабельная эстакада в базе данных отсутствует"));
    }
}
