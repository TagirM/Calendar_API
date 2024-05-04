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
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell())).getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell())).getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell())).getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell())).getResourceForWorkDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell())).getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell())).getResourceForEstDoc();
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
