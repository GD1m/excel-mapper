package ru.gdim.excelmapper.mapper.driver.annotation;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;
import ru.gdim.excelmapper.mapper.driver.annotation.exception.InstantiateTypeException;
import ru.gdim.excelmapper.mapper.format.FormatUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public final class AnnotationBasedExcelMappingDriver<T> implements ExcelMappingDriver<T> { // TODO separate lib?

    private final Class<T> type;

    public AnnotationBasedExcelMappingDriver(Class<T> type) {

        if (type == null) {

            throw new IllegalArgumentException("'type' can not be null");
        }

        this.type = type;
    }

    /**
     * Получить коллекцию колонок, используемых в таблице excel
     *
     * @return коллекция представлений excel колонок
     */
    @Override
    public Collection<ExcelColumn> getColumns() {

        Set<ExcelColumn> columns = new HashSet<>();

        Field[] declaredFields = type.getDeclaredFields();

        for (Field field : declaredFields) {

            ExcelValue annotation = field.getAnnotation(ExcelValue.class);

            if (annotation != null) {

                String columnName = annotation.columnName();
                boolean isRequired = annotation.isRequired();

                columns.add(
                        new GenericExcelColumn(
                                columnName,
                                isRequired,
                                field.getType()
                        )
                );
            }
        }

        return columns;
    }

    /**
     * Импорт строки excel
     *
     * @param row       строка excel
     * @param columnBag контейнер найденных колонок по заголовку
     * @return DTO с импортированными данными
     * @throws InvalidCellFormatException    если в ячейке excel некорректное значение
     * @throws RequiredColumnMissedException если в строке excel не было найдено значение обязательной колонки
     *                                       ({@link ExcelColumn#isRequired()} )
     */
    @Override
    public T readData(Row row, ColumnHeaderBag columnBag)
            throws InvalidCellFormatException, RequiredColumnMissedException {

        T result = instantiateType();

        for (ExcelColumn column : getColumns()) {

            GenericExcelColumn genericExcelColumn = (GenericExcelColumn) column;

            Cell cell = columnBag.getCellFromRow(row, genericExcelColumn);

            log.info("{}", genericExcelColumn);
            log.info("{}", cell);

            ObjectMapper objectMapper = new ObjectMapper();

            T result2;

            TypeFactory typeFactory = TypeFactory.defaultInstance();

            JavaType javaType = typeFactory.constructType(type);

            result2 = objectMapper.convertValue(FormatUtils.stringValue(cell), type);

            // TODO map to T / construct and settters

            log.info("{}", result2);
        }

        return result;
    }

    private T instantiateType() {

        try {

            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {

            throw new InstantiateTypeException(e);
        }
    }

}
