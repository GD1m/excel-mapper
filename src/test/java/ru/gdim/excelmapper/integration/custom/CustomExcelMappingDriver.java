package ru.gdim.excelmapper.integration.custom;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;
import ru.gdim.excelmapper.exception.RequiredColumnMissedException;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;
import ru.gdim.excelmapper.mapper.format.BigDecimalFormatter;
import ru.gdim.excelmapper.mapper.format.LocalDateFormatter;
import ru.gdim.excelmapper.mapper.format.LongFormatter;
import ru.gdim.excelmapper.mapper.format.ValueFormatterProvider;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Драйвер импорта тестовых данных из excel
 */
@RequiredArgsConstructor
public final class CustomExcelMappingDriver implements ExcelMappingDriver<SampleParsedRow> {

    /**
     * Получить коллекцию колонок, используемых в таблице excel
     *
     * @return коллекция представлений excel колонок
     */
    @Override
    public Set<ExcelColumn> getColumns() {

        return Stream
                .of(SampleColumns.values())
                .collect(Collectors.toSet());
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
    public SampleParsedRow readData(Row row, ColumnHeaderBag columnBag, ValueFormatterProvider valueFormatterProvider)
            throws InvalidCellFormatException, RequiredColumnMissedException {

        boolean isRowNotBlank = false;

        SampleParsedRow dto = new SampleParsedRow();

        Cell longValue = columnBag.getCellFromRow(row, SampleColumns.LONG);
        if (longValue != null) {

            dto.setLongValue(new LongFormatter().format(longValue));
            isRowNotBlank = true;
        }

        Cell bigDecimal = columnBag.getCellFromRow(row, SampleColumns.BIG_DECIMAL);
        if (bigDecimal != null) {

            dto.setBigDecimal(new BigDecimalFormatter().format(bigDecimal));
            isRowNotBlank = true;
        }

        Cell date = columnBag.getCellFromRow(row, SampleColumns.DATE_AFTER_BLANK);
        if (date != null) {

            LocalDate localDate = new LocalDateFormatter().format(date);
            dto.setLocalDate(localDate); // TODO refactor
            if (localDate != null) {
                isRowNotBlank = true;

            }
        }

        return (isRowNotBlank) ? dto : null;
    }

}
