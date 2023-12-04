package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;

public class DoubleFormatter implements ValueFormatter<Double> {

    @Override
    public Class<Double> valueType() {

        return Double.class;
    }

    @Override
    public Double format(Cell cell) {

        return (cell != null) ? cell.getNumericCellValue() : null;
    }

}
