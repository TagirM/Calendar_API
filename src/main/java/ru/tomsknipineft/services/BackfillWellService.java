package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.repositories.BackfillWellRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class BackfillWellService implements EntityProjectService{

    private final BackfillWellRepository backfillWellRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell())).getResource();
        }
        return 0;
    }

    /**
     * Получение сущности (Инженерная подготовка куста) из БД
     * @return сущность (Инженерная подготовка куста)
     */
    public BackfillWell getFirst(){
        return backfillWellRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Инженерная подготовка куста в базе данных отсутствует"));
    }
}
