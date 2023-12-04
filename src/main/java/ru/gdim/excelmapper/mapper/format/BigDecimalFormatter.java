package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;

public class BigDecimalFormatter implements ValueFormatter<BigDecimal> {

    @Override
    public Class<BigDecimal> valueType() {

        return BigDecimal.class;
    }

    @Override
    public BigDecimal format(Cell cell) {

        Double value = new DoubleFormatter().format(cell); // TODO handle blank value ("" to 0.0)

        return (value != null) ? BigDecimal.valueOf(value) : null;
    }

}
