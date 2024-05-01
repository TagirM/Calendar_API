package ru.tomsknipineft.entities.oilPad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.utils.entityValidator.EngineeringSurveyOilPadGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveEngineeringSurvey;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
Объект, который включает в себя все сооружения инженерной подготовки кустовой площадки
 */
@GroupSequenceProvider(EngineeringSurveyOilPadGroupSequenceProvider.class)
@Data
public class DataFormOilPad implements DataFormProject, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Шифр не может быть пустым")
    @Size(min = 4, max = 10, message = "Шифр должен иметь количество символов от 4 до 10")
    String codeContract;

    @NotNull(message = "Дата не может быть пустой")
    @FutureOrPresent(message = "Указана прошедшая дата")
    LocalDate startContract;

    @Valid
    private BackfillWell backfillWell = new BackfillWell();

    @Valid
    private Road road = new Road();

    @Valid
    private Line line = new Line();

    @Valid
    private List<BackfillSite> backfillSites = new ArrayList<>();

    @Valid
    private Vvp vvp = new Vvp();

    @Valid
    private CableRack cableRack = new CableRack();

    private boolean fieldEngineeringSurvey;

    private boolean engineeringSurveyReport;

    @NotNull(message = "Заполните количество буровых бригад", groups = OnActiveEngineeringSurvey.class)
    @Min(value = 1, message = "Количество буровых бригад не может быть меньше 1", groups = OnActiveEngineeringSurvey.class)
    @Max(value = 10, message = "Количество буровых бригад не должно быть больше 10", groups = OnActiveEngineeringSurvey.class)
    private Integer drillingRig;

    @NotNull(message = "Заполните человеческий фактор")
    @Min(value = 0, message = "Человеческий фактор не может быть меньше 0")
    @Max(value = 100, message = "Человеческий фактор не должен быть больше 100")
    private Integer humanFactor;
}
