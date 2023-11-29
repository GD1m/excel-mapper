package ru.gdim.excelmapper.exception;

public final class ValueFormatterNotFoundException extends ExcelMapperException {

    public ValueFormatterNotFoundException(Class<?> type) {

        super("ValueFormatter not found for type: " + type); // TODO for/of type
    }

}
