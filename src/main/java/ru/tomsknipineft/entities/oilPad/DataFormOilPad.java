package ru.tomsknipineft.entities.oilPad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.areaObjects.Vec;
import ru.tomsknipineft.entities.areaObjects.Vjk;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.utils.entityValidator.EngineeringSurveyGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveEngineeringSurvey;

import java.time.LocalDate;

/**
Объект, который включает в себя все сооружения инженерной подготовки кустовой площадки
 */
@GroupSequenceProvider(EngineeringSurveyGroupSequenceProvider.class)
@Data
public class DataFormOilPad {

    @NotNull(message = "The code should not be empty")
    @Size(min = 4, max = 10, message = "Code should be between 4 and 10 characters")
    String codeContract;

    @NotNull(message = "The date should not be empty")
    @FutureOrPresent(message = "The date can not be past")
    LocalDate startContract;

    @Valid
    private BackfillWell backfillWell = new BackfillWell();

    @Valid
    private Road road = new Road();

    @Valid
    private Line line = new Line();

    @Valid
    private Mupn mupn = new Mupn();

    @Valid
    private Vec vec = new Vec();

    @Valid
    private Vvp vvp = new Vvp();

    @Valid
    private CableRack cableRack = new CableRack();

    @Valid
    private Vjk vjk = new Vjk();

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
