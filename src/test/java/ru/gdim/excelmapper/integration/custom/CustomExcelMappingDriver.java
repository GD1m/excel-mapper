package ru.gdim.excelmapper.integration.custom;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;
import ru.gdim.excelmapper.mapper.format.BigDecimalFormatter;
import ru.gdim.excelmapper.mapper.format.LocalDateFormatter;
import ru.gdim.excelmapper.mapper.format.LongFormatter;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Драйвер импорта тестовых данных из excel
 */
public class CustomExcelMappingDriver implements ExcelMappingDriver<SampleParsedRow> {

    /**
     * Получить коллекцию колонок, используемых в таблице excel
     *
     * @return коллекция представлений excel колонок
     */
    @Override
    public Set<ExcelColumn> getExpectedColumns() {

        return Stream
                .of(SampleColumns.values())
                .collect(Collectors.toSet());
    }

    /**
     * Импорт строки excel (Логика мапинга строки excel в заданный тип <T>)
     *
     * @param row            строка excel
     * @param foundColumnBag контейнер найденных колонок по заголовкам
     * @return DTO с импортированными данными
     * @throws RequiredColumnMissedException если в строке excel не было найдено значение обязательной колонки
     *                                       ({@link ExcelColumn#isRequired()} )
     */
    @Override
    public SampleParsedRow readData(Row row, ColumnHeaderBag foundColumnBag) throws RequiredColumnMissedException {

        boolean isRowBlank = true;

        SampleParsedRow dto = new SampleParsedRow();

        Cell longValue = foundColumnBag.getCellFromRow(row, SampleColumns.INT_COLUMN);
        if (longValue != null) {

            dto.setLongValue(new LongFormatter().format(longValue));
            isRowBlank = false;
        }

        Cell bigDecimal = foundColumnBag.getCellFromRow(row, SampleColumns.FLOAT_COLUMN);
        if (bigDecimal != null) {

            dto.setBigDecimal(new BigDecimalFormatter().format(bigDecimal));
            isRowBlank = false;
        }

        Cell date = foundColumnBag.getCellFromRow(row, SampleColumns.BIG_DECIMAL_COLUMN);
        if (date != null) {

            LocalDate localDate = new LocalDateFormatter().format(date);
            dto.setLocalDate(localDate); // TODO refactor
            if (localDate != null) {
                isRowBlank = false;

            }
        }

        return (isRowBlank) ? null : dto;
    }

}
