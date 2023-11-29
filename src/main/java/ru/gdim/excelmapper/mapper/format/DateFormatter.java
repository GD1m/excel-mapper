package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;

import java.util.Date;

public class DateFormatter implements ValueFormatter<Date> {

    @Override
    public Class<Date> type() {

        return Date.class;
    }

    @Override
    public Date format(Cell cell) throws InvalidCellFormatException {

        try {

            return (cell != null) ? cell.getDateCellValue() : null;
        } catch (Exception e) {

            throw new InvalidCellFormatException(cell, e);
        }
    }

}
