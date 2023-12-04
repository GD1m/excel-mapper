package ru.gdim.excelmapper.exception;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

public class CellProcessingException extends RowProcessingException {

    private final CellReference cellReference;

    public CellProcessingException(Cell cell, Throwable cause) {

        super(
                cell.getRow(),
                "Не удалось обработать ячейку (адрес: " + cell.getAddress() + "): " + cause.getMessage(),
                cause
        );

        cellReference = new CellReference(cell.getRowIndex(), cell.getColumnIndex());
    }

    public CellReference getCellReference() {

        return cellReference;
    }

}
