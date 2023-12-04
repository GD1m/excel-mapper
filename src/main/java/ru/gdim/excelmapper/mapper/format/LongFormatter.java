package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;

public class LongFormatter implements ValueFormatter<Long> {

    @Override
    public Class<Long> valueType() {

        return Long.class;
    }

    @Override
    public Long format(Cell cell) {

        Double value = new DoubleFormatter().format(cell);

        return (value != null) ? value.longValue() : null;
    }

}
