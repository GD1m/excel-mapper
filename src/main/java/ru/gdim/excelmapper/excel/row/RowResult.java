package ru.gdim.excelmapper.excel.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Результат импорта excel строки
 *
 * @param <T> тип объекта с импортированными данными
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class RowResult<T> {

    /**
     * Индекс excel строки (начиная с 0, пустые строки пропускаются)
     */
    private long rowIndex;

    private RowResultStatus status;

    /**
     * Результат с импортированными данными
     */
    private T data;

    /**
     * Объект исключения
     */
    private Exception error;

}
