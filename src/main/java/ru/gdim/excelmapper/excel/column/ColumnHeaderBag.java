package ru.gdim.excelmapper.excel.column;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.header.ColumnHeaderReference;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;

import java.util.Collection;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Контейнер найденных заголовков колонок
 */
public final class ColumnHeaderBag {

    /**
     * Найденные заголовки колонок
     */
    private final Collection<ColumnHeaderReference> foundColumnHeaders;

    public ColumnHeaderBag(Collection<ColumnHeaderReference> foundColumnHeaders) {

        this.foundColumnHeaders = foundColumnHeaders;
    }

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

        Cell cell = (columnIndex != null)
                ? row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)
                : null;

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

        if (column.isRequired() && cell == null) {

            throw new RequiredColumnMissedException(row, column);
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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof ColumnHeaderBag)) return false;

        ColumnHeaderBag that = (ColumnHeaderBag) o;

        return Objects.equals(foundColumnHeaders, that.foundColumnHeaders);
    }

    @Override
    public int hashCode() {

        return Objects.hash(foundColumnHeaders);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", ColumnHeaderBag.class.getSimpleName() + "[", "]")
                .add("foundColumnHeaders=" + foundColumnHeaders)
                .toString();
    }

}
