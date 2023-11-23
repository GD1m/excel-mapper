package ru.gdim.excelmapper;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.gdim.excelmapper.custom.CustomExcelMappingDriver;
import ru.gdim.excelmapper.custom.SampleParsedRow;
import ru.gdim.excelmapper.excel.column.header.provider.ColumnHeaderProvider;
import ru.gdim.excelmapper.excel.column.header.provider.FirstRowColumnHeaderProvider;
import ru.gdim.excelmapper.excel.row.MappedRow;
import ru.gdim.excelmapper.excel.row.RowStatus;
import ru.gdim.excelmapper.excel.workbook.WorkbookFactory;
import ru.gdim.excelmapper.excel.workbook.XSSFWorkbookFactory;
import ru.gdim.excelmapper.exception.ColumnHeadersNotFoundException;
import ru.gdim.excelmapper.mapper.MappedResult;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomExcelMappingTest {

    private static final String SAMPLE_EXCEL_FILE_PATH = "./sample/sample.xlsx";

    private final WorkbookFactory workbookFactory = new XSSFWorkbookFactory();
    private final ColumnHeaderProvider columnHeaderProvider = new FirstRowColumnHeaderProvider();
    private final ExcelMappingDriver<SampleParsedRow> excelMappingDriver = new CustomExcelMappingDriver();

    private File file;
    private ExcelMapper<SampleParsedRow> excelMapper;

    @BeforeEach
    void setUp() {

        file = new File(SAMPLE_EXCEL_FILE_PATH);
        excelMapper = new ExcelMapper<>(workbookFactory, columnHeaderProvider, excelMappingDriver);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sampleImportDriver() throws ColumnHeadersNotFoundException, IOException, InvalidFormatException {

        MappedResult<SampleParsedRow> result = excelMapper.read(file, "Лист1");
        assertNotNull(result);

        Set<MappedRow<SampleParsedRow>> rows = result.getRows();
        assertNotNull(rows);

        assertEquals(result.getSuccessRowCount(), rows.size());
        assertEquals(result.getFailedRowCount(), 0);

        rows.forEach(row -> {

            assertSame(row.getStatus(), RowStatus.SUCCESS);
            assertNotNull(row.getData());
        });
    }

}
