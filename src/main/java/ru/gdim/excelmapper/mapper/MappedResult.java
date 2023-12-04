package ru.gdim.excelmapper.mapper;

import ru.gdim.excelmapper.excel.row.RowResult;
import ru.gdim.excelmapper.excel.row.RowResultStatus;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Результат мапинга excel файла
 *
 * @param <T> тип объекта с импортированными данными
 */
public final class MappedResult<T> {

    private final Collection<RowResult<T>> rows;
    private final MappedStatistic statistic;

    public MappedResult(Collection<RowResult<T>> rows, MappedStatistic statistic) {

        this.rows = rows;
        this.statistic = statistic;
    }

    public Collection<RowResult<T>> getRows() {

        return rows;
    }

    public MappedStatistic getStatistic() {

        return statistic;
    }

    @SuppressWarnings("unused")
    public Collection<RowResult<T>> getSuccessRows() {

        return rows
                .stream()
                .filter(row -> row.getStatus() == RowResultStatus.SUCCESS)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof MappedResult)) return false;

        MappedResult<?> that = (MappedResult<?>) o;

        return Objects.equals(rows, that.rows) && Objects.equals(statistic, that.statistic);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rows, statistic);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", MappedResult.class.getSimpleName() + "[", "]")
                .add("rows=" + rows)
                .add("statistic=" + statistic)
                .toString();
    }

}
