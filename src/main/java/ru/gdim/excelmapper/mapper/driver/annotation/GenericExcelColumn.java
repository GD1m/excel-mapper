package ru.gdim.excelmapper.mapper.driver.annotation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.mapper.format.ValueFormatter;

import java.lang.reflect.Field;
import java.util.Arrays;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public final class GenericExcelColumn implements ExcelColumn {

    private final String headerTitle;
    private final boolean required;
    private final String fieldName;
    private final Class<?> valueType;
    private final Class<? extends ValueFormatter<?>> valueFormatterType;

    public GenericExcelColumn(ExcelValue annotation, Field field) {

        headerTitle = annotation.columnHeaderTitle();
        required = annotation.isRequired();
        fieldName = field.getName();
        valueType = field.getType();

        Class<? extends ValueFormatter<?>>[] valueFormatters = annotation.valueFormatter();
        valueFormatterType = Arrays
                .stream(valueFormatters)
                .findFirst()
                .orElse(null);
    }

}
