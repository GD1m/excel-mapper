package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateFormatter implements ValueFormatter<LocalDate> {

    @Override
    public Class<LocalDate> type() {

        return LocalDate.class;
    }

    @Override
    public LocalDate format(Cell cell) throws InvalidCellFormatException {

        Date value = new DateFormatter().format(cell);

        return (value != null)
                ? value
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                : null;
    }

}
