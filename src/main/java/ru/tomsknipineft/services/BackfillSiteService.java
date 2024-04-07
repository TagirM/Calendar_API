package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.repositories.BackfillSiteRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class BackfillSiteService {

    private final BackfillSiteRepository backfillSiteRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param backfillSite Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceBackfillSite(BackfillSite backfillSite){
        if (backfillSite.isActive()){
            return backfillSiteRepository.findFirstBySquareGreaterThanEqual(backfillSite.getSquare()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение площади " + backfillSite.getSquare())).getResource();
        }
        return 0;
    }

    /**
     * Получение сущности (Инженерная подготовка площадки) из БД
     * @return сущность (Инженерная подготовка площадки)
     */
    public BackfillSite getFirst(){
        return backfillSiteRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Площадка в базе данных отсутствует"));
    }
}
