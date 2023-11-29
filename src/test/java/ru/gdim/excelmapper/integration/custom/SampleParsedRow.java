package ru.gdim.excelmapper.integration.custom;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public final class SampleParsedRow {

    private Long longValue;
    private BigDecimal bigDecimal;
    private LocalDate localDate;

}
