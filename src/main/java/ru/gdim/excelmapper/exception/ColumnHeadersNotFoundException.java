package ru.gdim.excelmapper.exception;

/**
 * Не найдены заголовки колонок в таблице excel
 */
public final class ColumnHeadersNotFoundException extends Exception {

    public ColumnHeadersNotFoundException() {

        super("Заголовки колонок не найдены");
    }

}
