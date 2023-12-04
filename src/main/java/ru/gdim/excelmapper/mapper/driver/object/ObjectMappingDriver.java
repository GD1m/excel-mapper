package ru.gdim.excelmapper.mapper.driver.object;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.exception.CellProcessingException;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.exception.RowProcessingException;
import ru.gdim.excelmapper.exception.ValueFormatterNotFoundException;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;
import ru.gdim.excelmapper.mapper.driver.object.exception.IllegalValueTypeException;
import ru.gdim.excelmapper.mapper.driver.object.exception.ResultTypeConstructorException;
import ru.gdim.excelmapper.mapper.driver.object.exception.ResultTypeSetterException;
import ru.gdim.excelmapper.mapper.driver.object.util.StringUtils;
import ru.gdim.excelmapper.mapper.format.ValueFormatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

public class ObjectMappingDriver<T> implements ExcelMappingDriver<T> { // TODO separate lib?

    private final Class<T> resultType;
    private final ValueFormatterProvider valueFormatterProvider;

    private Collection<ExcelColumn> expectedColumns;

    public ObjectMappingDriver(Class<T> resultType, ValueFormatterProvider valueFormatterProvider) {

        if (resultType == null) {

            throw new IllegalArgumentException("'resultType' can not be null");
        }

        if (valueFormatterProvider == null) {

            throw new IllegalArgumentException("'valueFormatterProvider' can not be null");
        }

        this.resultType = resultType;
        this.valueFormatterProvider = valueFormatterProvider;
    }

    /**
     * Получить коллекцию колонок, используемых в таблице excel
     *
     * @return коллекция представлений excel колонок
     */
    @Override
    public Collection<ExcelColumn> getExpectedColumns() {

        if (expectedColumns == null) {

            initColumns();
        }

        return expectedColumns;
    }

    /**
     * Импорт строки excel
     *
     * @param row            строка excel
     * @param foundColumnBag контейнер найденных колонок по заголовкам
     * @return DTO с импортированными данными
     * @throws RowProcessingException        если в строке excel некорректное значение
     * @throws RequiredColumnMissedException если в строке excel не было найдено значение обязательной колонки
     *                                       ({@link ExcelColumn#isRequired()} )
     */
    @Override
    public T readData(Row row, ColumnHeaderBag foundColumnBag)
            throws RowProcessingException, RequiredColumnMissedException {

        T result = null;

        for (ExcelColumn column : expectedColumns) {

            try {

                result = processColumn(result, row, foundColumnBag, (GenericExcelColumn) column);
            } catch (ResultTypeConstructorException | ResultTypeSetterException e) {

                throw new RowProcessingException(row, e);
            }
        }

        return result;
    }

    private void initColumns() {

        expectedColumns = new HashSet<>();

        Field[] declaredFields = resultType.getDeclaredFields();

        for (Field field : declaredFields) {

            ExcelValue annotation = field.getAnnotation(ExcelValue.class);

            if (annotation != null) {

                expectedColumns.add(
                        new GenericExcelColumn(annotation, field)
                );
            }
        }
    }

    private T processColumn(
            T result,
            Row row,
            ColumnHeaderBag columnBag,
            GenericExcelColumn genericColumn
    ) throws RequiredColumnMissedException,
            CellProcessingException,
            ResultTypeConstructorException,
            ResultTypeSetterException {

        Object value = readColumn(row, columnBag, genericColumn);

        if (value == null) {

            return result;
        }

        if (result == null) {

            result = instantiateResultType(); // instantiate only if any value exists
        }

        assignFieldValue(result, genericColumn, value);

        return result;
    }

    private Object readColumn(Row row, ColumnHeaderBag columnBag, GenericExcelColumn genericColumn)
            throws RequiredColumnMissedException, CellProcessingException {

        Cell cell = columnBag.getCellFromRow(row, genericColumn, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);

        if (cell == null) {

            return null;
        }

        return processCell(cell, genericColumn);
    }

    private Object processCell(Cell cell, GenericExcelColumn genericColumn) throws CellProcessingException {

        ValueFormatter<?> valueFormatter;
        try {

            valueFormatter = initValueFormatter(genericColumn);
        } catch (ValueFormatterNotFoundException | IllegalValueTypeException | RuntimeException e) {

            throw new CellProcessingException(cell, e);
        }

        return formatValue(cell, valueFormatter);
    }

    private ValueFormatter<?> initValueFormatter(GenericExcelColumn genericColumn)
            throws ValueFormatterNotFoundException, IllegalValueTypeException {

        Class<?> valueType = genericColumn.getValueType();
        Class<? extends ValueFormatter<?>> forcedValueFormatterType = genericColumn.getValueFormatterType();

        ValueFormatter<?> valueFormatter;

        if (forcedValueFormatterType != null) {

            valueFormatter = valueFormatterProvider.getFormatterOfType(forcedValueFormatterType);
            validateValueFormatter(valueType, valueFormatter);
        } else {

            valueFormatter = valueFormatterProvider.getFormatterForType(valueType);
        }

        return valueFormatter;
    }

    private Object formatValue(Cell cell, ValueFormatter<?> valueFormatter) throws CellProcessingException {

        try {

            return valueFormatter.format(cell);
        } catch (RuntimeException e) {

            throw new CellProcessingException(cell, e);
        }
    }

    private void validateValueFormatter(Class<?> valueType, ValueFormatter<?> valueFormatter)
            throws IllegalValueTypeException {

        Class<?> valueFormatterValueType = valueFormatter.valueType();

        if (!valueType.isAssignableFrom(valueFormatterValueType)) {

            throw new IllegalValueTypeException(valueType, valueFormatterValueType);
        }
    }

    private T instantiateResultType() throws ResultTypeConstructorException {

        try {

            Constructor<T> resultTypeConstructor = resultType.getDeclaredConstructor();

            return resultTypeConstructor.newInstance();
        } catch (
                NoSuchMethodException
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | RuntimeException e
        ) {

            throw new ResultTypeConstructorException(e);
        }
    }

    private void assignFieldValue(T result, GenericExcelColumn genericColumn, Object value)
            throws ResultTypeSetterException {

        Method setter = getSetter(genericColumn);

        invokeSetter(setter, value, result);
    }

    private Method getSetter(GenericExcelColumn genericColumn) throws ResultTypeSetterException {

        try {

            String fieldName = genericColumn.getFieldName();
            Class<?> valueType = genericColumn.getValueType();

            return resultType.getDeclaredMethod(
                    composeSetterName(fieldName),
                    valueType
            );
        } catch (NoSuchMethodException | RuntimeException e) {

            throw new ResultTypeSetterException(e);
        }
    }

    private String composeSetterName(String fieldName) {

        return "set" + StringUtils.capitalize(fieldName);
    }

    private void invokeSetter(Method setter, Object value, T object) throws ResultTypeSetterException {

        try {

            setter.invoke(object, value);
        } catch (InvocationTargetException | IllegalAccessException | RuntimeException e) {

            throw new ResultTypeSetterException(e);
        }
    }

}
