package ru.gdim.excelmapper.mapper.driver;

import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.mapper.format.ValueFormatterProvider;

import java.util.Collection;

/**
 * Драйвер мапинга данных из строки excel
 *
 * @param <T> тип объекта с импортированными данными
 */
public interface ExcelMappingDriver<T> {

    /**
     * Получить коллекцию колонок, используемых в таблице excel (какие колонки таблицы нужно парсить)
     *
     * @return коллекция представлений excel колонок
     */
    Collection<ExcelColumn> getColumns();

    /**
     * Импорт строки excel (Логика мапинга строки excel в заданный тип <T>)
     *
     * @param row                    строка excel
     * @param columnBag              контейнер найденных колонок по заголовку
     * @param valueFormatterProvider // TODO
     * @return DTO с импортированными данными
     * @throws InvalidCellFormatException    если в ячейке excel некорректное значение
     * @throws RequiredColumnMissedException если в строке excel не было найдено значение обязательной колонки
     *                                       ({@link ExcelColumn#isRequired()} )
     */
    T readData(Row row, ColumnHeaderBag columnBag, ValueFormatterProvider valueFormatterProvider)
            throws InvalidCellFormatException, RequiredColumnMissedException;

}
