package ru.gdim.excelmapper.integration.annotation;

import ru.gdim.excelmapper.mapper.driver.annotation.ExcelValue;
import ru.gdim.excelmapper.mapper.format.LocalDateFormatter;
import ru.gdim.excelmapper.mapper.format.StringFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

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

    public Long getLongValue() {

        return longValue;
    }

    public void setLongValue(Long longValue) {

        this.longValue = longValue;
    }

    public BigDecimal getBigDecimal() {

        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {

        this.bigDecimal = bigDecimal;
    }

    public LocalDate getLocalDate() {

        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {

        this.localDate = localDate;
    }

    public String getString() {

        return string;
    }

    public void setString(String string) {

        this.string = string;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof AnnotatedRow)) return false;

        AnnotatedRow that = (AnnotatedRow) o;

        return Objects.equals(longValue, that.longValue)
                && Objects.equals(bigDecimal, that.bigDecimal)
                && Objects.equals(localDate, that.localDate)
                && Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {

        return Objects.hash(longValue, bigDecimal, localDate, string);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", AnnotatedRow.class.getSimpleName() + "[", "]")
                .add("longValue=" + longValue)
                .add("bigDecimal=" + bigDecimal)
                .add("localDate=" + localDate)
                .add("string='" + string + "'")
                .toString();
    }

}
