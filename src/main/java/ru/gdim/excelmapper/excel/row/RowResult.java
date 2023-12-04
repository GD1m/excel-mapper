package ru.gdim.excelmapper.excel.row;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Результат импорта excel строки
 *
 * @param <T> тип объекта с импортированными данными
 */
public final class RowResult<T> {

    /**
     * Индекс excel строки (начиная с 0, пустые строки пропускаются)
     */
    private final long rowIndex;

    private final RowResultStatus status;

    /**
     * Результат с импортированными данными
     */
    private final T data;

    /**
     * Объект исключения
     */
    private final Exception error;

    public RowResult(long rowIndex, RowResultStatus status, T data, Exception error) {

        this.rowIndex = rowIndex;
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public long getRowIndex() {

        return rowIndex;
    }

    public RowResultStatus getStatus() {

        return status;
    }

    public T getData() {

        return data;
    }

    public Exception getError() {

        return error;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof RowResult)) return false;

        RowResult<?> rowResult = (RowResult<?>) o;

        return rowIndex == rowResult.rowIndex
                && status == rowResult.status
                && Objects.equals(data, rowResult.data)
                && Objects.equals(error, rowResult.error);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rowIndex, status, data, error);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", RowResult.class.getSimpleName() + "[", "]")
                .add("rowIndex=" + rowIndex)
                .add("status=" + status)
                .add("data=" + data)
                .add("error=" + error)
                .toString();
    }

}
