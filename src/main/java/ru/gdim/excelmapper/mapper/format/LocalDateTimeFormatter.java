package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeFormatter implements ValueFormatter<LocalDateTime> {

    @Override
    public Class<LocalDateTime> valueType() {

        return LocalDateTime.class;
    }

    @Override
    public LocalDateTime format(Cell cell) {

        Date value = new DateFormatter().format(cell);

        return (value != null)
                ? value
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                : null;
    }

}
