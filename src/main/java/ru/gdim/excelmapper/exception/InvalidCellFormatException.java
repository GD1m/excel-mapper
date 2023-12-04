package ru.gdim.excelmapper.exception;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

/**
 * Некорректный формат excel ячейки
 */
public final class InvalidCellFormatException extends ExcelMapperException { // TODO rename or create separate CellProcessingException ?

    private final CellReference cellReference;

    public InvalidCellFormatException(Cell cell, Throwable cause) {

        super(
                "Некорректный формат ячейки (адрес: " + cell.getAddress() + "): " + cause.getMessage(),
                cause
        );

        cellReference = new CellReference(cell.getRowIndex(), cell.getColumnIndex());
    }

    @SuppressWarnings("unused")
    public CellReference getCellReference() {

        return cellReference;
    }

}
