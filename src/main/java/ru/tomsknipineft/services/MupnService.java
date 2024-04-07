package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.oilPad.Mupn;
import ru.tomsknipineft.repositories.MupnRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class MupnService implements EntityProjectService{

    private final MupnRepository mupnRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param mupn МУПН
     * @return количество необходимого ресурса
     */
    public Integer getResourceMupn(Mupn  mupn){
        if (mupn.isActive()){
            return mupnRepository.findFirstBySquareGreaterThanEqual(mupn.getSquare()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение площади " + mupn.getSquare())).getResource();
        }
        return 0;
    }

    /**
     * Получение сущности (площадка МУПН) из БД
     * @return сущность (площадка МУПН)
     */
    public Mupn getFirst(){
        return mupnRepository.findById(1L).orElseThrow();
    }
}
