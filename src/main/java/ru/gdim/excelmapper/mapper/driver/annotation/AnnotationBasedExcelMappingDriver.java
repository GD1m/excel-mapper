package ru.gdim.excelmapper.mapper.driver.annotation;

import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;

import java.util.Collection;

public final class AnnotationBasedExcelMappingDriver<T> implements ExcelMappingDriver<T> { // TODO implement

    /**
     * Получить коллекцию колонок, используемых в таблице excel
     *
     * @return коллекция представлений excel колонок
     */
    @Override
    public Collection<ExcelColumn> getColumns() {

        return null;
    }

    /**
     * Импорт строки excel
     *
     * @param row       строка excel
     * @param columnBag контейнер найденных колонок по заголовку
     * @return DTO с импортированными данными
     * @throws InvalidCellFormatException    если в ячейке excel некорректное значение
     * @throws RequiredColumnMissedException если в строке excel не было найдено значение обязательной колонки
     *                                       ({@link ExcelColumn#isRequired()} )
     */
    @Override
    public T readData(Row row, ColumnHeaderBag columnBag)
            throws InvalidCellFormatException, RequiredColumnMissedException {

        return null;
    }

}
