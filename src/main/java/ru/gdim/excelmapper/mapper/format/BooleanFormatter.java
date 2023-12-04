package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class BooleanFormatter implements ValueFormatter<Boolean> {


    @Override
    public Class<Boolean> valueType() {

        return Boolean.class;
    }

    @Override
    public Boolean format(Cell cell) {

        if (cell == null) {

            return null;
        }

        if (cell.getCellType() == CellType.BOOLEAN) {

            return cell.getBooleanCellValue();
        }

        String stringValue = new StringFormatter().format(cell);

        if (stringValue == null || stringValue.isEmpty()) {

            return null;
        }

        if (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false")) {

            return Boolean.parseBoolean(stringValue);
        }

        if (stringValue.equals("+") || stringValue.equals("1")) {

            return Boolean.TRUE;
        }

        if (stringValue.equals("-") || stringValue.equals("0")) {

            return Boolean.FALSE;
        }

        throw new IllegalArgumentException("Cannot parse Boolean from unexpected value: '" + stringValue + "'");
    }

}
