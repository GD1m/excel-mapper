package ru.gdim.excelmapper.integration.annotation;

import ru.gdim.excelmapper.mapper.driver.object.ExcelValue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class AnnotatedRow {

    @ExcelValue(columnHeaderTitle = "Int column", isRequired = false, valueFormatter = {})
    private Long longValue;

    @ExcelValue(columnHeaderTitle = "Float column", isRequired = false, valueFormatter = {})
    private Float floatValue;

    @ExcelValue(columnHeaderTitle = "Big Decimal column", isRequired = false, valueFormatter = {})
    private BigDecimal bigDecimal;

    @ExcelValue(
            columnHeaderTitle = "Datetime column",
            isRequired = false
    )
    private LocalDateTime localDateTime;

    @ExcelValue(
            columnHeaderTitle = "Date column",
            isRequired = false
    )
    private LocalDate localDate;

    @ExcelValue(columnHeaderTitle = "Boolean column")
    private Boolean booleanValue;

    @ExcelValue(columnHeaderTitle = "Колонка UTF-8 column")
    private String stringValue;

    public Long getLongValue() {

        return longValue;
    }

    public void setLongValue(Long longValue) {

        this.longValue = longValue;
    }

    public Float getFloatValue() {

        return floatValue;
    }

    public void setFloatValue(Float floatValue) {

        this.floatValue = floatValue;
    }

    public BigDecimal getBigDecimal() {

        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {

        this.bigDecimal = bigDecimal;
    }

    public LocalDateTime getLocalDateTime() {

        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {

        this.localDateTime = localDateTime;
    }

    public LocalDate getLocalDate() {

        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {

        this.localDate = localDate;
    }

    public Boolean getBooleanValue() {

        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {

        this.booleanValue = booleanValue;
    }

    public String getStringValue() {

        return stringValue;
    }

    public void setStringValue(String stringValue) {

        this.stringValue = stringValue;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof AnnotatedRow)) return false;

        AnnotatedRow that = (AnnotatedRow) o;

        return Objects.equals(longValue, that.longValue)
                && Objects.equals(floatValue, that.floatValue)
                && Objects.equals(bigDecimal, that.bigDecimal)
                && Objects.equals(localDateTime, that.localDateTime)
                && Objects.equals(localDate, that.localDate)
                && Objects.equals(booleanValue, that.booleanValue)
                && Objects.equals(stringValue, that.stringValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(longValue, floatValue, bigDecimal, localDateTime, localDate, booleanValue, stringValue);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", AnnotatedRow.class.getSimpleName() + "[", "]")
                .add("longValue=" + longValue)
                .add("floatValue=" + floatValue)
                .add("bigDecimal=" + bigDecimal)
                .add("localDateTime=" + localDateTime)
                .add("localDate=" + localDate)
                .add("booleanValue=" + booleanValue)
                .add("stringValue='" + stringValue + "'")
                .toString();
    }

}
