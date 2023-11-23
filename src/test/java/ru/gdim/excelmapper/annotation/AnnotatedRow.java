package ru.gdim.excelmapper.annotation;

import lombok.Data;
import ru.gdim.excelmapper.mapper.driver.annotation.Annotation;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public final class AnnotatedRow {

    @Annotation(columnName = "Название колонки 1")
    private Long longValue;

    @Annotation(columnName = "Название колонки 2", isRequired = true)
    private BigDecimal bigDecimal;

    @Annotation(columnName = "Название колонки 3", isRequired = false)
    private LocalDate localDate;

}
