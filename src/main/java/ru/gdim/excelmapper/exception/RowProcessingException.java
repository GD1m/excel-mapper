package ru.gdim.excelmapper.exception;

import org.apache.poi.ss.usermodel.Row;

public class RowProcessingException extends ExcelMapperException {

    public RowProcessingException(Row row, Throwable cause) {

        this(row, "", cause);

    }

    public RowProcessingException(Row row, String message, Throwable cause) {

        super(
                "Не удалось обработать строку с индексом '" + row.getRowNum() + "': " + cause.getMessage() // TODO refactor message
                        + ": " + message,
                cause
        );

    }

}
