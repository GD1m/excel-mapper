package ru.gdim.excelmapper.integration.annotation;

import lombok.Data;
import ru.gdim.excelmapper.mapper.driver.annotation.ExcelValue;
import ru.gdim.excelmapper.mapper.format.LocalDateFormatter;
import ru.gdim.excelmapper.mapper.format.StringFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public final class AnnotatedRow {

    @ExcelValue(columnHeaderTitle = "Колонка long", isRequired = true, valueFormatter = {})
    private Long longValue;

    @ExcelValue(columnHeaderTitle = "Колонка big decimal", isRequired = false, valueFormatter = {})
    private BigDecimal bigDecimal;

    @ExcelValue(
            columnHeaderTitle = "Колонка после пропуска с датой",
            isRequired = false,
            valueFormatter = LocalDateFormatter.class
    )
    private LocalDate localDate;

    @ExcelValue(
            columnHeaderTitle = "10.10.2023 0:00:00",
            isRequired = false,
            valueFormatter = StringFormatter.class
    )
    private String string;

}
