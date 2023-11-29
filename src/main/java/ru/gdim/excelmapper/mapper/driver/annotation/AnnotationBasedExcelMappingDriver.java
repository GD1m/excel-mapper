package ru.gdim.excelmapper.mapper.driver.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.exception.ValueFormatterNotFoundException;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;
import ru.gdim.excelmapper.mapper.driver.annotation.exception.InstantiateTypeException;
import ru.gdim.excelmapper.mapper.format.ValueFormatter;
import ru.gdim.excelmapper.mapper.format.ValueFormatterProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public final class AnnotationBasedExcelMappingDriver<T> implements ExcelMappingDriver<T> { // TODO separate lib?

    private final Class<T> resultType;
    private final Constructor<T> resultTypeConstructor;

    public AnnotationBasedExcelMappingDriver(Class<T> resultType) {

        if (resultType == null) {

            throw new IllegalArgumentException("'resultType' can not be null");
        }

        this.resultType = resultType;
        try {

            this.resultTypeConstructor = resultType.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {

            throw new InstantiateTypeException(e);
        }
    }

    /**
     * Получить коллекцию колонок, используемых в таблице excel
     *
     * @return коллекция представлений excel колонок
     */
    @Override
    public Collection<ExcelColumn> getColumns() {

        Set<ExcelColumn> columns = new HashSet<>();

        Field[] declaredFields = resultType.getDeclaredFields();

        for (Field field : declaredFields) {

            ExcelValue annotation = field.getAnnotation(ExcelValue.class);

            if (annotation != null) {

                columns.add(
                        new GenericExcelColumn(annotation, field)
                );
            }
        }

        return columns;
    }

    /**
     * Импорт строки excel
     *
     * @param row                    строка excel
     * @param columnBag              контейнер найденных колонок по заголовку
     * @param valueFormatterProvider
     * @return DTO с импортированными данными
     * @throws InvalidCellFormatException    если в ячейке excel некорректное значение
     * @throws RequiredColumnMissedException если в строке excel не было найдено значение обязательной колонки
     *                                       ({@link ExcelColumn#isRequired()} )
     */
    @Override
    public T readData(Row row, ColumnHeaderBag columnBag, ValueFormatterProvider valueFormatterProvider)
            throws InvalidCellFormatException, RequiredColumnMissedException {

        T result = null;

        for (ExcelColumn column : getColumns()) {

            GenericExcelColumn genericColumn = (GenericExcelColumn) column;

            Object value = readColumn(row, columnBag, valueFormatterProvider, genericColumn);

            if (value == null) {

                continue;
            }

            if (result == null) {

                result = instantiateResultType();
            }

            assignValueToResult(result, genericColumn, value);
        }

        return result;
    }

    private T instantiateResultType() {

        try {

            return resultTypeConstructor.newInstance();
        } catch (Exception e) {

            throw new InstantiateTypeException(e);
        }
    }

    private Object readColumn(
            Row row,
            ColumnHeaderBag columnBag,
            ValueFormatterProvider valueFormatterProvider,
            GenericExcelColumn genericColumn
    ) throws RequiredColumnMissedException, InvalidCellFormatException {

        Cell cell = columnBag.getCellFromRow(row, genericColumn);

        if (cell == null) {

            return null;
        }

        return processCell(cell, genericColumn, valueFormatterProvider);
    }

    private Object processCell(
            Cell cell,
            GenericExcelColumn genericColumn,
            ValueFormatterProvider valueFormatterProvider
    ) throws InvalidCellFormatException {

        ValueFormatter<?> valueFormatter;
        try {

            valueFormatter = initValueFormatter(genericColumn, valueFormatterProvider);
        } catch (ValueFormatterNotFoundException e) {

            throw new RuntimeException(e); // TODO Refactor
        }

        return valueFormatter.format(cell);
    }

    private ValueFormatter<?> initValueFormatter(
            GenericExcelColumn genericColumn,
            ValueFormatterProvider valueFormatterProvider
    ) throws ValueFormatterNotFoundException {

        Class<? extends ValueFormatter<?>> valueFormatterType = genericColumn.getValueFormatterType();

        if (valueFormatterType != null) {

            return valueFormatterProvider.getFormatterOfType(valueFormatterType);
        }

        return valueFormatterProvider.getFormatterForType(genericColumn.getValueType());
    }

    private void assignValueToResult(T result, GenericExcelColumn genericColumn, Object value) {

        Method setter = composeSetter(
                genericColumn.getFieldName(),
                genericColumn.getValueType()
        );

        invokeSetter(setter, value, result);
    }

    private Method composeSetter(String fieldName, Class<?> valueType) {

        Method setter;

        try {

            setter = resultType.getDeclaredMethod(
                    composeSetterName(fieldName),
                    valueType
            );
        } catch (NoSuchMethodException e) {

            throw new RuntimeException(e); // TODO exception
        }

        return setter;
    }

    private String composeSetterName(String fieldName) {

        return "set" + StringUtils.capitalize(fieldName);
    }

    private void invokeSetter(Method setter, Object value, T result) {

        try {

            setter.invoke(result, value);
        } catch (IllegalAccessException | InvocationTargetException e) {

            throw new RuntimeException(e); // TODO exception
        }
    }

}
