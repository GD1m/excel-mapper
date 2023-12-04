package ru.gdim.excelmapper.exception;

/**
 * Не найдены заголовки колонок в таблице excel
 */
public class ColumnHeadersNotFoundException extends ExcelMapperException {

    public ColumnHeadersNotFoundException() {

        super("Заголовки колонок не найдены");
    }

}
