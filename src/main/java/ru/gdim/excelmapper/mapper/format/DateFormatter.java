package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;

import java.util.Date;

public class DateFormatter implements ValueFormatter<Date> {

    @Override
    public Class<Date> valueType() {

        return Date.class;
    }

    @Override
    public Date format(Cell cell) {

        return (cell != null) ? cell.getDateCellValue() : null;
    }

}
