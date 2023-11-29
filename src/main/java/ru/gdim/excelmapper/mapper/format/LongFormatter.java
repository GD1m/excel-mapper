package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;

public class LongFormatter implements ValueFormatter<Long> {

    @Override
    public Class<Long> type() {

        return Long.class;
    }

    @Override
    public Long format(Cell cell) throws InvalidCellFormatException {

        Double value = DoubleFormatter.formatValue(cell);

        return (value != null) ? value.longValue() : null;
    }

}
