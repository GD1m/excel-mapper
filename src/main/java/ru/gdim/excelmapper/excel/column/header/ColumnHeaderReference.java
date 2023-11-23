package ru.gdim.excelmapper.excel.column.header;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.excel.column.ExcelColumn;

import java.util.Objects;

/**
 * Данные о заголовке колонки таблицы excel
 */
@Data
@RequiredArgsConstructor
public final class ColumnHeaderReference {

    /**
     * Колонка excel
     */
    private final ExcelColumn column;

    /**
     * Ячейка таблицы, в которой найден заголовок колонки
     */
    private final Cell cell;

    public int getRowIndex() {

        return Objects.requireNonNull(cell).getRowIndex();
    }

    public int getColumnIndex() {

        return Objects.requireNonNull(cell).getColumnIndex();
    }

}
