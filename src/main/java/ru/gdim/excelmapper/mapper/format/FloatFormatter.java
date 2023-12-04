package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;

public class FloatFormatter implements ValueFormatter<Float> {


    @Override
    public Class<Float> valueType() {

        return Float.class;
    }

    @Override
    public Float format(Cell cell) {

        Double value = new DoubleFormatter().format(cell);

        return (value != null) ? value.floatValue() : null;
    }

}
