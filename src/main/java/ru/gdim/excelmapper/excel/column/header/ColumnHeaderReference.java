package ru.gdim.excelmapper.excel.column.header;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.excel.column.ExcelColumn;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Данные о заголовке колонки таблицы excel
 */
public final class ColumnHeaderReference {

    /**
     * Колонка excel
     */
    private final ExcelColumn column;

    /**
     * Ячейка таблицы, в которой найден заголовок колонки
     */
    private final Cell cell;

    public ColumnHeaderReference(ExcelColumn column, Cell cell) {

        this.column = column;
        this.cell = cell;
    }

    public ExcelColumn getColumn() {

        return column;
    }

    @SuppressWarnings("unused")
    public Cell getCell() {

        return cell;
    }

    public int getRowIndex() {

        return Objects.requireNonNull(cell).getRowIndex();
    }

    public int getColumnIndex() {

        return Objects.requireNonNull(cell).getColumnIndex();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof ColumnHeaderReference)) return false;

        ColumnHeaderReference that = (ColumnHeaderReference) o;

        return column.equals(that.column) && cell.equals(that.cell);
    }

    @Override
    public int hashCode() {

        return Objects.hash(column, cell);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", ColumnHeaderReference.class.getSimpleName() + "[", "]")
                .add("column=" + column)
                .add("cell=" + cell)
                .toString();
    }

}
