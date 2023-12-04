package ru.gdim.excelmapper.exception;

import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ExcelColumn;

/**
 * Не найдена обязательная колонка в строке
 */
public class RequiredColumnMissedException extends ExcelMapperException {

    public RequiredColumnMissedException(Row row, ExcelColumn column) {

        this(row, column, null);
    }

    public RequiredColumnMissedException(Row row, ExcelColumn column, Throwable cause) {

        super("Не найдена обязательная колонка '" + column.getHeaderTitle() + "' в строке c индексом: " + row.getRowNum(), cause);
    }

}
