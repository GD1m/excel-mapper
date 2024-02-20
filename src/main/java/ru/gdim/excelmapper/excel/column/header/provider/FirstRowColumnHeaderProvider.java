package ru.gdim.excelmapper.excel.column.header.provider;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.excel.column.header.ColumnHeaderReference;
import ru.gdim.excelmapper.mapper.format.StringFormatter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Ищет заголовки в первой непустой строке
 */
public class FirstRowColumnHeaderProvider implements ColumnHeaderProvider {

    private static final Logger log = LoggerFactory.getLogger(FirstRowColumnHeaderProvider.class);

    /**
     * Получить заголовки колонок листа таблицы excel
     *
     * @param sheet   лист excel
     * @param columns колонки таблицы excel
     * @return коллекция данных о заголовках
     */
    @Override
    public Collection<ColumnHeaderReference> getColumnHeaders(Sheet sheet, Collection<ExcelColumn> columns) {

        Row row = sheet.getRow(
                sheet.getFirstRowNum()
        );

        Set<ColumnHeaderReference> columnHeaders = new HashSet<>();

        if (row == null) {

            return columnHeaders;
        }

        log.debug("Поиск заголовков колонок в строке с индексом '{}'...", row.getRowNum());

        for (short columnIndex = row.getFirstCellNum(); columnIndex < row.getLastCellNum(); columnIndex++) {

            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (cell == null) {

                log.debug("Пропущена пустая ячейка '{}'", new CellAddress(row.getRowNum(), columnIndex));

                continue;
            }

            ExcelColumn foundColumn = processCell(cell, columns);

            if (foundColumn != null) {

                columnHeaders.add(
                        new ColumnHeaderReference(foundColumn, cell)
                );
            }
        }

        return columnHeaders;
    }

    /**
     * Обработка ячейки
     *
     * @param cell    ячейка
     * @param columns колонки таблицы excel
     * @return представление excel колонки, если какой-либо заголовок был найден в ячейке
     */
    private ExcelColumn processCell(Cell cell, Collection<ExcelColumn> columns) {

        String cellValue = parseCellValue(cell);

        if (cellValue == null) {

            return null;
        }

        ExcelColumn foundColumn = findColumnByTitle(cellValue, columns);

        if (foundColumn == null) {

            log.warn(
                    "Пропущена колонка с неизвестным заголовком '{}' в ячейке '{}'",
                    cellValue,
                    cell.getAddress()
            );

            return null;
        }

        log.debug("Найден заголовок колонки '{}' в ячейке '{}'", cellValue, cell.getAddress());

        return foundColumn;
    }

    /**
     * Получение значения ячейки
     *
     * @param cell ячейка
     * @return значение
     */
    private String parseCellValue(Cell cell) {

        try {

            return new StringFormatter().format(cell);
        } catch (RuntimeException e) {

            log.warn(
                    "Пропущена ячейка с некорректным форматом во время парсинга заголовков в ячейке'{}'",
                    cell.getAddress(),
                    e
            );

            return null;
        }
    }

    /**
     * Поиск колонки по заголовку
     *
     * @param columnHeaderTitle заголовок колонки
     * @param columns           колонки таблицы excel
     * @return представление excel колонки, если значение ячейки соответствует какому-либо заголовку
     */
    private ExcelColumn findColumnByTitle(String columnHeaderTitle, Collection<ExcelColumn> columns) {

        return columns
                .stream()
                .filter(column -> Objects.equals(column.getHeaderTitle(), columnHeaderTitle))
                .findFirst()
                .orElse(null);
    }

}
