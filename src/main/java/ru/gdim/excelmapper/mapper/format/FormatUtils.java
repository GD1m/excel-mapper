package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;
import ru.gdim.excelmapper.exception.ValueFormatterNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Утилита для парсинга разных типов данных из excel ячейки
 */
public final class FormatUtils { // TODO refactor/cleanup

    public static <T> T format(Cell cell, Class<T> valueType, ValueFormatterProvider valueFormatterProvider)
            throws ValueFormatterNotFoundException, InvalidCellFormatException {

        ValueFormatter<T> valueFormatter = valueFormatterProvider.getFormatterForType(valueType);

        return valueFormatter.format(cell);
    }

    public static String stringValue(Cell cell) throws InvalidCellFormatException {

        DataFormatter dataFormatter = new DataFormatter();

        try {

            return dataFormatter.formatCellValue(cell);
        } catch (Exception e) {

            throw new InvalidCellFormatException(cell, e);
        }
    }

    public static Double doubleValue(Cell cell) throws InvalidCellFormatException {

        try {

            return (cell != null) ? cell.getNumericCellValue() : null;
        } catch (Exception e) {

            throw new InvalidCellFormatException(cell, e);
        }
    }

    public static Integer intValue(Cell cell) throws InvalidCellFormatException {

        Double value = doubleValue(cell);

        return (value != null) ? value.intValue() : null;
    }

    public static Long longValue(Cell cell) throws InvalidCellFormatException {

        Double value = doubleValue(cell);

        return (value != null) ? value.longValue() : null;
    }

    public static BigDecimal bigDecimalValue(Cell cell) throws InvalidCellFormatException {

        Double value = doubleValue(cell); // TODO handle blank value ("" to 0.0)

        return (value != null) ? BigDecimal.valueOf(value) : null;
    }

    public static Date dateValue(Cell cell) throws InvalidCellFormatException {

        try {

            return (cell != null) ? cell.getDateCellValue() : null;
        } catch (Exception e) {

            throw new InvalidCellFormatException(cell, e);
        }
    }

    public static LocalDate localDateValue(Cell cell) throws InvalidCellFormatException {

        Date value = dateValue(cell);

        return (value != null)
                ? value
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                : null;
    }

    public static LocalDateTime localDateTimeValue(Cell cell) throws InvalidCellFormatException {

        Date value = dateValue(cell);

        return (value != null) ? LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()) : null;
    }

    public static ZonedDateTime zonedDateTimeValue(Cell cell) throws InvalidCellFormatException {

        Date value = dateValue(cell);

        return (value != null) ? ZonedDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault()) : null;
    }

}
