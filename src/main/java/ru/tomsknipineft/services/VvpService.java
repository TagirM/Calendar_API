package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.repositories.VvpRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class VvpService implements EntityProjectService{

    private final VvpRepository vvpRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param vvp Временная вертолетная площадка
     * @return количество необходимого ресурса
     */
    public Integer getResourceVvp(Vvp vvp){
        if (vvp.isActive()){
            return vvpRepository.findFirstBySquareGreaterThanEqualAndHelicopterModel(vvp.getSquare(),
                    vvp.getHelicopterModel()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение площади " + vvp.getSquare() +
                            " и/или модель вертолета " + vvp.getHelicopterModel())).getResource();
        }
        return 0;
    }

    /**
     * Получение сущности (Временная вертолетная площадка) из БД
     * @return сущность (Временная вертолетная площадка)
     */
    public Vvp getFirst(){
        return vvpRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Временная вертолетная площадка в базе данных отсутствует"));
    }
}
