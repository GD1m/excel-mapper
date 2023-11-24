package ru.gdim.excelmapper.annotation;

import lombok.Data;
import ru.gdim.excelmapper.mapper.driver.annotation.ExcelValue;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public final class AnnotatedRow {

    @ExcelValue(columnName = "Колонка long")
    private Long longValue;

    @ExcelValue(columnName = "Колонка big decimal", isRequired = false)
    private BigDecimal bigDecimal;

    @ExcelValue(columnName = "Колонка после пропуска с датой", isRequired = false)
    private LocalDate localDate;

}
