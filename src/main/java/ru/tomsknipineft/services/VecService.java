package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.Vec;
import ru.tomsknipineft.repositories.VecRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class VecService implements EntityProjectService{

    private final VecRepository vecRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param vec ВЭЦ
     * @return количество необходимого ресурса
     */
    public Integer getResourceVec(Vec vec){
        if (vec.isActive()){
            return vecRepository.findFirstByPowerAndSquareGreaterThanEqual(vec.getPower(), vec.getSquare()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение мощности " + vec.getPower() +" или площади " + vec.getSquare())).getResource();
        }
        return 0;
    }

    /**
     * Получение сущности (ВЭЦ) из БД
     * @return сущность (ВЭЦ)
     */
    public Vec getFirst(){
        return vecRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("ВЭЦ в базе данных отсутствует"));
    }
}
