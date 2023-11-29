package ru.gdim.excelmapper.mapper;

import lombok.Data;
import ru.gdim.excelmapper.excel.row.MappedRow;

import java.util.Set;

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
    private final Set<MappedRow<T>> rows;
    private final MappedStatistic statistic;

    public String prettyPrint() {

        return null; // TODO implement
    }

}
