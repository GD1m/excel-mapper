package ru.gdim.excelmapper.integration;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gdim.excelmapper.ExcelMapper;
import ru.gdim.excelmapper.excel.row.RowResult;
import ru.gdim.excelmapper.excel.row.RowResultStatus;
import ru.gdim.excelmapper.exception.ExcelMapperException;
import ru.gdim.excelmapper.integration.annotation.AnnotatedRow;
import ru.gdim.excelmapper.integration.custom.CustomExcelMappingDriver;
import ru.gdim.excelmapper.integration.custom.SampleParsedRow;
import ru.gdim.excelmapper.mapper.MappedResult;
import ru.gdim.excelmapper.mapper.MappedStatistic;
import ru.gdim.excelmapper.mapper.driver.object.ObjectMappingDriver;
import ru.gdim.excelmapper.mapper.driver.object.ValueFormatterProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ExcelMappingDriverTest {

    private static final String SAMPLE_EXCEL_FILE_PATH = "src/test/resources/sample/sample.xlsx";

    private File file;

    @BeforeEach
    void setUp() {

        file = new File(SAMPLE_EXCEL_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void annotationBasedExcelMappingDriver()
            throws ExcelMapperException, IOException, InvalidFormatException {

        ExcelMapper<AnnotatedRow> excelMapper = new ExcelMapper<>(
                new ObjectMappingDriver<>(
                        AnnotatedRow.class,
                        new ValueFormatterProvider()
                )
        );

        driverSmokeTest(excelMapper);
    }

    @Test
    void customExcelMappingDriver() throws ExcelMapperException, IOException, InvalidFormatException {

        ExcelMapper<SampleParsedRow> excelMapper = new ExcelMapper<>(new CustomExcelMappingDriver());

        driverSmokeTest(excelMapper);
    }

    private <T> void driverSmokeTest(ExcelMapper<T> excelMapper)
            throws IOException, InvalidFormatException, ExcelMapperException {

        MappedResult<T> result = excelMapper.read(file);

        assertNotNull(result);

        Collection<RowResult<T>> rows = result.getRows();
        assertNotNull(rows);

        MappedStatistic statistic = result.getStatistic();

        assertEquals(statistic.getSuccessRowCount(), rows.size());
        assertEquals(statistic.getFailedRowCount(), 0);

        rows.forEach(row -> {

            assertSame(row.getStatus(), RowResultStatus.SUCCESS);
            assertNotNull(row.getData());
        });
    }

}
