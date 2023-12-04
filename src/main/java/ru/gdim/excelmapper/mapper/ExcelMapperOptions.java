package ru.gdim.excelmapper.mapper;

import org.apache.poi.ss.usermodel.Row;

import java.util.Objects;
import java.util.StringJoiner;

public class ExcelMapperOptions {

    private boolean failOnError = false;
    private boolean failOnRequiredColumnMissed = false;
    private boolean haltOnBlankRow = true; // TODO remove?
    private int maxBlankRowsAtARow; // TODO name, implement ?

    private Row.MissingCellPolicy missingCellPolicy; // TODO implement ?

    public ExcelMapperOptions() {
    }

    public ExcelMapperOptions(boolean failOnError, boolean failOnRequiredColumnMissed, boolean haltOnBlankRow) {

        this.failOnError = failOnError;
        this.failOnRequiredColumnMissed = failOnRequiredColumnMissed;
        this.haltOnBlankRow = haltOnBlankRow;
    }

    public boolean isFailOnError() {

        return this.failOnError;
    }

    public ExcelMapperOptions setFailOnError(boolean failOnError) {

        this.failOnError = failOnError;

        return this;
    }

    public boolean isFailOnRequiredColumnMissed() {

        return this.failOnRequiredColumnMissed;
    }

    public ExcelMapperOptions setFailOnRequiredColumnMissed(boolean failOnRequiredColumnMissed) {

        this.failOnRequiredColumnMissed = failOnRequiredColumnMissed;

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

        return failOnError == that.failOnError
                && failOnRequiredColumnMissed == that.failOnRequiredColumnMissed
                && haltOnBlankRow == that.haltOnBlankRow;
    }

    @Override
    public int hashCode() {

        return Objects.hash(failOnError, failOnRequiredColumnMissed, haltOnBlankRow);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", ExcelMapperOptions.class.getSimpleName() + "[", "]")
                .add("failOnError=" + failOnError)
                .add("failOnRequiredColumnMissed=" + failOnRequiredColumnMissed)
                .add("haltOnBlankRow=" + haltOnBlankRow)
                .toString();
    }
}
