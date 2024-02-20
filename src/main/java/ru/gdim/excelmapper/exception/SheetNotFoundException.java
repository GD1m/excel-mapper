package ru.gdim.excelmapper.exception;

public class SheetNotFoundException extends ExcelMapperException {

    public static SheetNotFoundException byIndex(int sheetIndex) {

        return new SheetNotFoundException("Лист excel не найден по индексу: '" + sheetIndex + "'");
    }

    public static SheetNotFoundException byName(String sheetName) {

        return new SheetNotFoundException("Лист excel не найден по имени: '" + sheetName + "'");
    }

    protected SheetNotFoundException(String message) {

        super(message);
    }

}
