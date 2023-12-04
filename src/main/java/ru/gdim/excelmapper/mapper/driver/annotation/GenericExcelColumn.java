package ru.gdim.excelmapper.mapper.driver.annotation;

import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.mapper.format.ValueFormatter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public final class GenericExcelColumn implements ExcelColumn {

    private final String headerTitle;
    private final boolean required;
    private final String fieldName;
    private final Class<?> valueType;
    private final Class<? extends ValueFormatter<?>> valueFormatterType;

    public GenericExcelColumn(
            String headerTitle,
            boolean required,
            String fieldName,
            Class<?> valueType,
            Class<? extends ValueFormatter<?>> valueFormatterType
    ) {

        this.headerTitle = headerTitle;
        this.required = required;
        this.fieldName = fieldName;
        this.valueType = valueType;
        this.valueFormatterType = valueFormatterType;
    }

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

    @Override
    public String getHeaderTitle() {

        return headerTitle;
    }

    @Override
    public boolean isRequired() {

        return required;
    }

    public String getFieldName() {

        return fieldName;
    }

    public Class<?> getValueType() {

        return valueType;
    }

    public Class<? extends ValueFormatter<?>> getValueFormatterType() {

        return valueFormatterType;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof GenericExcelColumn)) return false;

        GenericExcelColumn that = (GenericExcelColumn) o;

        return required == that.required
                && Objects.equals(headerTitle, that.headerTitle)
                && Objects.equals(fieldName, that.fieldName)
                && Objects.equals(valueType, that.valueType)
                && Objects.equals(valueFormatterType, that.valueFormatterType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(headerTitle, required, fieldName, valueType, valueFormatterType);
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", GenericExcelColumn.class.getSimpleName() + "[", "]")
                .add("headerTitle='" + headerTitle + "'")
                .add("required=" + required)
                .add("fieldName='" + fieldName + "'")
                .add("valueType=" + valueType)
                .add("valueFormatterType=" + valueFormatterType)
                .toString();
    }

}
