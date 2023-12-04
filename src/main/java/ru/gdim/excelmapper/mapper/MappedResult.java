package ru.gdim.excelmapper.mapper;

import lombok.Data;
import ru.gdim.excelmapper.excel.row.RowResult;
import ru.gdim.excelmapper.excel.row.RowResultStatus;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Результат мапинга excel файла
 *
 * @param <T> тип объекта с импортированными данными
 */
@Data
public final class MappedResult<T> {

    /**
     * Результаты импорта excel строк
     */
    private final Collection<RowResult<T>> rows;
    private final MappedStatistic statistic;

    public Collection<RowResult<T>> getSuccessRows() {

        return rows
                .stream()
                .filter(row -> row.getStatus() == RowResultStatus.SUCCESS)
                .collect(Collectors.toList());
    }

    public String prettyPrint() {

        return null; // TODO implement
    }

}
