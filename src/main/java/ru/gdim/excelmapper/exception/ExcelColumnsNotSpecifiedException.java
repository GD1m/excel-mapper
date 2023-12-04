package ru.gdim.excelmapper.exception;

public class ExcelColumnsNotSpecifiedException extends ExcelMapperException {

    public ExcelColumnsNotSpecifiedException() {

        super("Искомые колонки не найдены (check ExcelMappingDriver.getColumns implementation)");
    }

}
