package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;

public class StringFormatter implements ValueFormatter<String> {

    @Override
    public Class<String> type() {

        return String.class;
    }

    @Override
    public String format(Cell cell) throws InvalidCellFormatException {

        DataFormatter dataFormatter = new DataFormatter();

        try {

            return dataFormatter.formatCellValue(cell);
        } catch (RuntimeException e) {

            throw new InvalidCellFormatException(cell, e);
        }
    }

}
