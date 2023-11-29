package ru.gdim.excelmapper.integration;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gdim.excelmapper.ExcelMapper;
import ru.gdim.excelmapper.excel.column.header.provider.ColumnHeaderProvider;
import ru.gdim.excelmapper.excel.column.header.provider.FirstRowColumnHeaderProvider;
import ru.gdim.excelmapper.excel.row.MappedRow;
import ru.gdim.excelmapper.excel.row.RowStatus;
import ru.gdim.excelmapper.excel.workbook.WorkbookFactory;
import ru.gdim.excelmapper.excel.workbook.XSSFWorkbookFactory;
import ru.gdim.excelmapper.exception.ExcelMapperException;
import ru.gdim.excelmapper.integration.annotation.AnnotatedRow;
import ru.gdim.excelmapper.mapper.ExcelMapperOptions;
import ru.gdim.excelmapper.mapper.MappedResult;
import ru.gdim.excelmapper.mapper.MappedStatistic;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;
import ru.gdim.excelmapper.mapper.driver.annotation.AnnotationBasedExcelMappingDriver;
import ru.gdim.excelmapper.mapper.format.ValueFormatterProvider;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationBasedExcelMappingTest {

    private static final String SAMPLE_EXCEL_FILE_PATH = "./sample/sample.xlsx";

    private final WorkbookFactory workbookFactory = new XSSFWorkbookFactory();
    private final ColumnHeaderProvider columnHeaderProvider = new FirstRowColumnHeaderProvider();
    private final ExcelMappingDriver<AnnotatedRow> excelMappingDriver = new AnnotationBasedExcelMappingDriver<>(
            AnnotatedRow.class
    );
    private final ValueFormatterProvider valueFormatterProvider = new ValueFormatterProvider();

    private File file;
    private ExcelMapper<AnnotatedRow> excelMapper;

    @BeforeEach
    void setUp() {

        file = new File(SAMPLE_EXCEL_FILE_PATH);
        excelMapper = new ExcelMapper<>(
                excelMappingDriver,
                workbookFactory,
                columnHeaderProvider,
                valueFormatterProvider,
                new ExcelMapperOptions()
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sampleImportDriver() throws ExcelMapperException, IOException, InvalidFormatException {

        MappedResult<AnnotatedRow> result = excelMapper.read(file);
        assertNotNull(result);

        Set<MappedRow<AnnotatedRow>> rows = result.getRows();
        assertNotNull(rows);

        MappedStatistic statistic = result.getStatistic();
        assertEquals(statistic.getSuccessRowCount(), rows.size());
        assertEquals(statistic.getFailedRowCount(), 0);

        rows.forEach(row -> {

            assertSame(row.getStatus(), RowStatus.SUCCESS);
            assertNotNull(row.getData());
        });
    }

}
