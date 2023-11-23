package ru.gdim.excelmapper.exception;

public final class ExcelColumnsNotSpecifiedException extends ExcelMapperException {

    public ExcelColumnsNotSpecifiedException() {

        super("Искомые колонки не найдены (check ExcelMappingDriver.getColumns implementation)");
    }

}
