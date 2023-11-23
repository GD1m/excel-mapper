package ru.gdim.excelmapper.excel.column.header.provider;

import org.apache.poi.ss.usermodel.Sheet;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.excel.column.header.ColumnHeaderReference;

import java.util.Collection;

public interface ColumnHeaderProvider {

    /**
     * Получить заголовки колонок листа таблицы excel
     *
     * @param sheet   лист excel
     * @param columns колонки таблицы excel
     * @return коллекция данных о заголовках
     */
    Collection<ColumnHeaderReference> getColumnHeaders(Sheet sheet, Collection<ExcelColumn> columns);

}
