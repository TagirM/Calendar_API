package ru.tomsknipineft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.repositories.BackfillWellRepository;
import ru.tomsknipineft.services.BackfillWellService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BackfillWellServiceTest {

    @Mock
    private BackfillWellRepository backfillWellRepository;

    @InjectMocks
    private BackfillWellService backfillWellService;

    private BackfillWell testBackfillWell;

    @BeforeEach
    public void setUp() {
        // Создание тестовых данных
        testBackfillWell = new BackfillWell();
        testBackfillWell.setId(1L);
        testBackfillWell.setActive(true);
        testBackfillWell.setWell(5);
        testBackfillWell.setResourceForWorkDoc(100);
    }

    @Test
    public void testGetResourceBackfillWell() {
        // Подготовка моков для вызова методов репозитория
        when(backfillWellRepository.findFirstByWellGreaterThanEqual(testBackfillWell.getWell())).thenReturn(Optional.of(testBackfillWell));

        // Вызов тестируемого метода
        Integer result = backfillWellService.getResourceForWorkDocBackfillWell(testBackfillWell);

        // Проверка результата
        assertEquals(100, result);
    }

    @Test
    public void testGetFirst() {
        // Подготовка моков для вызова метода репозитория
        when(backfillWellRepository.findById(1L)).thenReturn(Optional.of(testBackfillWell));

        // Вызов тестируемого метода
        BackfillWell result = backfillWellService.getFirst();

        // Проверка результата
        assertEquals(testBackfillWell, result);
    }
}
