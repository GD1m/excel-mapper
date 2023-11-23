package ru.gdim.excelmapper.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.gdim.excelmapper.excel.row.MappedRow;

import java.util.Set;

/**
 * Результат мапинга excel файла
 *
 * @param <T> тип объекта с импортированными данными
 */
@Data
@AllArgsConstructor
public final class MappedResult<T> {

    /**
     * Результаты импорта excel строк
     */
    private final Set<MappedRow<T>> rows;

    /**
     * Количество успешно импортированных строк
     */
    private final long successRowCount;

    /**
     * Количество строк с ошибкой
     */
    private final long failedRowCount;

    public String prettyPrint() {

        return null; // TODO implement
    }

}
