package ru.gdim.excelmapper.excel.column;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.header.ColumnHeaderReference;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.mapper.format.FormatUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * Контейнер найденных заголовков колонок
 */
@Data
@RequiredArgsConstructor
public final class ColumnHeaderBag {

    /**
     * Найденные заголовки колонок
     */
    @Getter(AccessLevel.NONE)
    private final Collection<ColumnHeaderReference> foundColumnHeaders;

    /**
     * Получить ячейку таблицы из строки
     *
     * @param row    строка excel
     * @param column колонка excel
     * @return ячейка excel
     * @throws RequiredColumnMissedException если не найдена обязательная колонка
     */
    public Cell getCellFromRow(Row row, ExcelColumn column) throws RequiredColumnMissedException {

        Integer columnIndex = getColumnIndex(column);

        Cell cell = (columnIndex != null) ? row.getCell(columnIndex) : null;

        checkBlankIfColumnRequired(row, column, cell);

        return cell;
    }

    /**
     * Получить индекс колонки
     *
     * @param column колонка
     * @return индекс
     */
    private Integer getColumnIndex(ExcelColumn column) {

        ColumnHeaderReference columnHeaderReference = getColumnHeaderReference(column);

        return (columnHeaderReference != null) ? columnHeaderReference.getColumnIndex() : null;
    }

    private void checkBlankIfColumnRequired(Row row, ExcelColumn column, Cell cell) throws RequiredColumnMissedException {

        try {

            if (column.isRequired() && FormatUtils.isBlank(cell)) {

                throw new RequiredColumnMissedException(row, column);
            }
        } catch (InvalidCellFormatException e) {

            throw new RequiredColumnMissedException(row, column, e);
        }
    }

    /**
     * Получить данные о заголовке колонки
     *
     * @param column колонка
     * @return данные о заголовке
     */
    private ColumnHeaderReference getColumnHeaderReference(ExcelColumn column) {

        return foundColumnHeaders
                .stream()
                .filter(columnHeader -> Objects.equals(columnHeader.getColumn(), column))
                .findFirst()
                .orElse(null);
    }

}
