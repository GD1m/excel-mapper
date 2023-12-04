package ru.gdim.excelmapper.integration;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import ru.gdim.excelmapper.ExcelMapper;
import ru.gdim.excelmapper.excel.column.header.provider.FirstRowColumnHeaderProvider;
import ru.gdim.excelmapper.excel.row.RowResult;
import ru.gdim.excelmapper.excel.workbook.XSSFWorkbookFactory;
import ru.gdim.excelmapper.exception.ExcelMapperException;
import ru.gdim.excelmapper.integration.annotation.AnnotatedRow;
import ru.gdim.excelmapper.integration.custom.CustomExcelMappingDriver;
import ru.gdim.excelmapper.integration.custom.SampleParsedRow;
import ru.gdim.excelmapper.mapper.ExcelMapperOptions;
import ru.gdim.excelmapper.mapper.MappedResult;
import ru.gdim.excelmapper.mapper.MappedStatistic;
import ru.gdim.excelmapper.mapper.driver.annotation.AnnotationBasedExcelMappingDriver;
import ru.gdim.excelmapper.mapper.format.ValueFormatterProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExcelMappingDriverTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExcelMappingDriverTest.class);
    private static final String SAMPLE_EXCEL_FILE_PATH = "./sample/sample.xlsx";

    private File file;

    @BeforeEach
    void setUp() {

        file = new File(SAMPLE_EXCEL_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void annotationBasedExcelMappingDriver() throws ExcelMapperException, IOException, InvalidFormatException {

        ExcelMapper<AnnotatedRow> excelMapper = new ExcelMapper<>(
                new AnnotationBasedExcelMappingDriver<>(
                        AnnotatedRow.class
                ),
                new XSSFWorkbookFactory(),
                new FirstRowColumnHeaderProvider(),
                new ValueFormatterProvider(),
                new ExcelMapperOptions(false, false, true)
        );

        smokeTestDriver(excelMapper);
    }

    @Test
    void annotationBasedExcelMappingDriverDoNotHaltOnBlank() throws ExcelMapperException, IOException, InvalidFormatException {

        ExcelMapper<AnnotatedRow> excelMapper = new ExcelMapper<>(
                new AnnotationBasedExcelMappingDriver<>(
                        AnnotatedRow.class
                ),
                new XSSFWorkbookFactory(),
                new FirstRowColumnHeaderProvider(),
                new ValueFormatterProvider(),
                new ExcelMapperOptions(false, false, false)
        );

        smokeTestDriver(excelMapper);
    }

    @Test
    void customExcelMappingDriver() throws ExcelMapperException, IOException, InvalidFormatException {

        ExcelMapper<SampleParsedRow> excelMapper = new ExcelMapper<>(
                new CustomExcelMappingDriver(),
                new XSSFWorkbookFactory(),
                new FirstRowColumnHeaderProvider(),
                new ValueFormatterProvider(),
                new ExcelMapperOptions()
        );

        smokeTestDriver(excelMapper);
    }

    private <T> void smokeTestDriver(ExcelMapper<T> excelMapper)
            throws IOException, InvalidFormatException, ExcelMapperException {

        MappedResult<T> result = excelMapper.read(file);

        assertNotNull(result);

        Collection<RowResult<T>> rows = result.getRows();
        assertNotNull(rows);

        MappedStatistic statistic = result.getStatistic();
//        assertEquals(statistic.getSuccessRowCount(), rows.size());
//        assertEquals(statistic.getFailedRowCount(), 0);

        rows.forEach(row -> {

//            assertSame(row.getStatus(), RowResultStatus.SUCCESS);
//            assertNotNull(row.getData());
        });
    }

}
