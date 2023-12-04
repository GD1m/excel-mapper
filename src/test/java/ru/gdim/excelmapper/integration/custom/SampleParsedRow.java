package ru.gdim.excelmapper.integration.custom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public final class SampleParsedRow {

    private Long longValue;
    private BigDecimal bigDecimal;
    private LocalDate localDate;

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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof SampleParsedRow)) return false;

        SampleParsedRow that = (SampleParsedRow) o;

        return Objects.equals(longValue, that.longValue)
                && Objects.equals(bigDecimal, that.bigDecimal)
                && Objects.equals(localDate, that.localDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(longValue, bigDecimal, localDate);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", SampleParsedRow.class.getSimpleName() + "[", "]")
                .add("longValue=" + longValue)
                .add("bigDecimal=" + bigDecimal)
                .add("localDate=" + localDate)
                .toString();
    }

}
