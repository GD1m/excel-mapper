package ru.gdim.excelmapper.mapper.driver.object.exception;

public class IllegalValueTypeException extends Exception {

    public IllegalValueTypeException(Class<?> valueType, Class<?> valueFormatterType) {

        super(
                String.format(
                        "Value type '%s' is not assignable from ValueFormatter type '%s'",
                        valueType,
                        valueFormatterType
                )
        );
    }

}
