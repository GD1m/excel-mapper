package ru.gdim.excelmapper.mapper;

import java.util.Objects;
import java.util.StringJoiner;

public final class MappedStatistic {

    /**
     * Количество успешно импортированных строк
     */
    private long successRowCount = 0;

    /**
     * Количество строк с ошибкой
     */
    private long failedRowCount = 0;

    private long blankRowCount = 0;

    private long requiredColumnMissedRowCount = 0;

    public long getSuccessRowCount() {

        return successRowCount;
    }

    public void setSuccessRowCount(long successRowCount) {

        this.successRowCount = successRowCount;
    }

    public long getFailedRowCount() {

        return failedRowCount;
    }

    public void setFailedRowCount(long failedRowCount) {

        this.failedRowCount = failedRowCount;
    }

    public long getBlankRowCount() {

        return blankRowCount;
    }

    public void setBlankRowCount(long blankRowCount) {

        this.blankRowCount = blankRowCount;
    }

    public long getRequiredColumnMissedRowCount() {

        return requiredColumnMissedRowCount;
    }

    public void setRequiredColumnMissedRowCount(long requiredColumnMissedRowCount) {

        this.requiredColumnMissedRowCount = requiredColumnMissedRowCount;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof MappedStatistic)) return false;

        MappedStatistic that = (MappedStatistic) o;

        return successRowCount == that.successRowCount
                && failedRowCount == that.failedRowCount
                && blankRowCount == that.blankRowCount
                && requiredColumnMissedRowCount == that.requiredColumnMissedRowCount;
    }

    @Override
    public int hashCode() {

        return Objects.hash(successRowCount, failedRowCount, blankRowCount, requiredColumnMissedRowCount);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", MappedStatistic.class.getSimpleName() + "[", "]")
                .add("successRowCount=" + successRowCount)
                .add("failedRowCount=" + failedRowCount)
                .add("blankRowCount=" + blankRowCount)
                .add("requiredColumnMissedRowCount=" + requiredColumnMissedRowCount)
                .toString();
    }

}
