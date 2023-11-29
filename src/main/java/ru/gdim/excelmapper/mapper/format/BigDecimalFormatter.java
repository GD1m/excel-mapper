package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;

import java.math.BigDecimal;

public class BigDecimalFormatter implements ValueFormatter<BigDecimal> {

    @Override
    public Class<BigDecimal> type() {

        return BigDecimal.class;
    }

    @Override
    public BigDecimal format(Cell cell) throws InvalidCellFormatException {

        Double value = DoubleFormatter.formatValue(cell); // TODO handle blank value ("" to 0.0)

        return (value != null) ? BigDecimal.valueOf(value) : null;
    }

}
