package ru.gdim.excelmapper.mapper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public final class ExcelMapperOptions {

    private boolean failOnError = false;
    private boolean failOnRequiredIsMissed = false;
    private boolean haltOnBlankRow = true;

    public ExcelMapperOptions() {
    }

    public ExcelMapperOptions(boolean failOnError, boolean failOnRequiredIsMissed, boolean haltOnBlankRow) {

        this.failOnError = failOnError;
        this.failOnRequiredIsMissed = failOnRequiredIsMissed;
        this.haltOnBlankRow = haltOnBlankRow;
    }

    public boolean isFailOnError() {

        return this.failOnError;
    }

    public ExcelMapperOptions setFailOnError(boolean failOnError) {

        this.failOnError = failOnError;

        return this;
    }

    public boolean isFailOnRequiredIsMissed() {

        return this.failOnRequiredIsMissed;
    }

    public ExcelMapperOptions setFailOnRequiredIsMissed(boolean failOnRequiredIsMissed) {

        this.failOnRequiredIsMissed = failOnRequiredIsMissed;

        return this;
    }

    public boolean isHaltOnBlankRow() {

        return this.haltOnBlankRow;
    }

    public ExcelMapperOptions setHaltOnBlankRow(boolean haltOnBlankRow) {

        this.haltOnBlankRow = haltOnBlankRow;

        return this;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof ExcelMapperOptions)) return false;

        ExcelMapperOptions that = (ExcelMapperOptions) o;

        return new EqualsBuilder()
                .append(failOnError, that.failOnError)
                .append(failOnRequiredIsMissed, that.failOnRequiredIsMissed)
                .append(haltOnBlankRow, that.haltOnBlankRow)
                .isEquals();
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(this);
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this)
                .append("failOnError", failOnError)
                .append("failOnRequiredIsMissed", failOnRequiredIsMissed)
                .append("haltOnBlankRow", haltOnBlankRow)
                .toString();
    }

}
