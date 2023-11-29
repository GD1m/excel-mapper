package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;

public class DoubleFormatter implements ValueFormatter<Double> {

    private static final DoubleFormatter INSTANCE = new DoubleFormatter();

    public static Double formatValue(Cell cell) throws InvalidCellFormatException {

        return INSTANCE.format(cell);
    }

    @Override
    public Class<Double> type() {

        return Double.class;
    }

    @Override
    public Double format(Cell cell) throws InvalidCellFormatException {

        try {

            return (cell != null) ? cell.getNumericCellValue() : null;
        } catch (Exception e) {

            throw new InvalidCellFormatException(cell, e);
        }
    }

}
