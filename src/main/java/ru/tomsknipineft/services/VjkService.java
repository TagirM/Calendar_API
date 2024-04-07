package ru.tomsknipineft.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.Vjk;
import ru.tomsknipineft.repositories.VjkRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class VjkService implements EntityProjectService{

    private final VjkRepository vjkRepository;

    /**
     * Поиск в БД количества ресурса необходимого для проектирования
     * @param vjk ВЖК
     * @return количество необходимого ресурса
     */
    public Integer getResourceVjk(Vjk vjk){
        if (vjk.isActive()){
            return vjkRepository.findFirstBySquareGreaterThanEqual(vjk.getSquare()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение площади ВЖК " + vjk.getSquare())).getResource();
        }
        return 0;
    }

    /**
     * Получение сущности (ВЖК) из БД
     * @return сущность (ВЖК)
     */
    public Vjk getFirst(){
        return vjkRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("ВЖК в базе данных отсутствует"));
    }
}
