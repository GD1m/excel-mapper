package ru.gdim.excelmapper.integration.annotation;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.mapper.format.BooleanFormatter;
import ru.gdim.excelmapper.mapper.format.StringFormatter;

public final class CustomBooleanFormatter extends BooleanFormatter {

    @Override
    public Boolean format(Cell cell) {

        String stringValue = new StringFormatter().format(cell);

        if (stringValue == null || stringValue.isEmpty()) {

            return null;
        }

        if (stringValue.equals("Да")) {

            return Boolean.TRUE;
        }

        if (stringValue.equals("Нет")) {

            return Boolean.FALSE;
        }

        return super.format(cell);
    }

}
