package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class StringFormatter implements ValueFormatter<String> {

    @Override
    public Class<String> valueType() {

        return String.class;
    }

    @Override
    public String format(Cell cell) {

        DataFormatter dataFormatter = new DataFormatter();

        return dataFormatter.formatCellValue(cell);
    }

}
