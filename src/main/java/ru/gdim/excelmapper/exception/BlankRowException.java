package ru.gdim.excelmapper.exception;

public final class BlankRowException extends ExcelMapperException {

    public BlankRowException(int rowIndex) {

        this(rowIndex, null);
    }

    public BlankRowException(int rowIndex, Throwable cause) {

        super("Пустая строка: " + rowIndex, cause);
    }
}
