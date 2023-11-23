package ru.gdim.excelmapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.excel.column.header.ColumnHeaderReference;
import ru.gdim.excelmapper.excel.column.header.provider.ColumnHeaderProvider;
import ru.gdim.excelmapper.excel.row.MappedRow;
import ru.gdim.excelmapper.excel.row.RowStatus;
import ru.gdim.excelmapper.excel.workbook.WorkbookFactory;
import ru.gdim.excelmapper.exception.*;
import ru.gdim.excelmapper.mapper.MappedResult;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Мапинг данных из excel
 *
 * @param <T> тип объекта с импортированными данными
 */
@RequiredArgsConstructor // TODO de lombok?
@Slf4j
public final class ExcelMapper<T> {

    private final WorkbookFactory workbookFactory;
    private final ColumnHeaderProvider columnHeaderFinder;
    private final ExcelMappingDriver<T> excelMappingDriver;

    @Getter
    @Setter
    private boolean terminateOnError = false;

    @Getter
    @Setter
    private boolean terminateOnRequiredIsMissed = false;

    @Getter
    @Setter
    private boolean haltOnBlankRow = false;

    /**
     * Обработка excel файла
     *
     * @param path путь excel файла
     * @return результат мапинга
     */
    public MappedResult<T> read(String path)
            throws ExcelMapperException, IOException, InvalidFormatException {

        return read(path, 0);
    }

    public MappedResult<T> read(String path, int sheetIndex)
            throws ExcelMapperException, IOException, InvalidFormatException {

        return processWorkBook(
                createWorkBook(path),
                sheetIndex
        );
    }

    public MappedResult<T> read(String path, String sheetName)
            throws ExcelMapperException, IOException, InvalidFormatException {

        return processWorkBook(
                createWorkBook(path),
                sheetName
        );
    }

    /**
     * Импорт данных из excel файла
     *
     * @param file excel файл
     * @return результат мапинга
     */
    public MappedResult<T> read(File file)
            throws ExcelMapperException, IOException, InvalidFormatException {

        return read(file, 0);
    }

    public MappedResult<T> read(File file, int sheetIndex)
            throws ExcelMapperException, IOException, InvalidFormatException {

        return processWorkBook(
                createWorkBook(file),
                sheetIndex
        );
    }

    public MappedResult<T> read(File file, String sheetName)
            throws ExcelMapperException, IOException, InvalidFormatException {

        return processWorkBook(
                createWorkBook(file),
                sheetName
        );
    }

    /**
     * Обработка InputStream excel файла
     *
     * @param inputStream InputStream excel файла
     * @return результат мапинга
     */
    public MappedResult<T> read(InputStream inputStream)
            throws IOException, ExcelMapperException {

        return read(inputStream, 0);
    }

    public MappedResult<T> read(InputStream inputStream, int sheetIndex)
            throws IOException, ExcelMapperException {

        return processWorkBook(
                createWorkBook(inputStream),
                sheetIndex
        );
    }

    public MappedResult<T> read(InputStream inputStream, String sheetName)
            throws ExcelMapperException, IOException {

        return processWorkBook(
                createWorkBook(inputStream),
                sheetName
        );
    }

    private Workbook createWorkBook(String path) throws IOException, InvalidFormatException {

        return workbookFactory.build(path);
    }

    private Workbook createWorkBook(File file) throws IOException, InvalidFormatException {

        return workbookFactory.build(file);
    }

    private Workbook createWorkBook(InputStream inputStream) throws IOException {

        return workbookFactory.build(inputStream);
    }

    /**
     * Обработка представления импортируемого excel файла
     *
     * @param workBook представление импортируемого файла
     * @return результат мапинга
     */
    private MappedResult<T> processWorkBook(Workbook workBook, int sheetIndex) throws ExcelMapperException {

        Sheet sheet = workBook.getSheetAt(sheetIndex);

        if (sheet == null) {

            throw new SheetNotFoundException("Лист excel не найден по индексу: '" + sheetIndex + "'");
        }

        return processSheet(sheet);
    }

    private MappedResult<T> processWorkBook(Workbook workBook, String sheetName) throws ExcelMapperException {

        Sheet sheet = workBook.getSheet(sheetName);

        if (sheet == null) {

            throw new SheetNotFoundException("Лист excel не найден по имени: '" + sheetName + "'");
        }

        return processSheet(sheet);
    }

    /**
     * Обработка листа excel
     *
     * @param sheet лист excel
     * @return результат мапинга
     * @throws ColumnHeadersNotFoundException если не были найдены заголовки колонок в таблице excel
     */
    private MappedResult<T> processSheet(Sheet sheet) throws ExcelMapperException {

        log.debug("Маппинг листа excel '{}' начат...", sheet.getSheetName());

        Collection<ExcelColumn> columns = excelMappingDriver.getColumns();

        if (CollectionUtils.isEmpty(columns)) {

            throw new ExcelColumnsNotSpecifiedException();
        }

        Collection<ColumnHeaderReference> foundColumnHeaders = columnHeaderFinder.getColumnHeaders(sheet, columns);

        if (CollectionUtils.isEmpty(foundColumnHeaders)) {

            throw new ColumnHeadersNotFoundException();
        }

        MappedResult<T> result = processRows(sheet, foundColumnHeaders);

        log.debug("Маппинг листа excel завершен: {}", result);

        return result;
    }

    private MappedResult<T> processRows(Sheet sheet, Collection<ColumnHeaderReference> foundColumnHeaders)
            throws ExcelMapperException {

        long successRowCount = 0;
        long failedRowCount = 0;

        Set<MappedRow<T>> rowResults = new LinkedHashSet<>();

        for (int rowIndex = composeStartRowIndex(foundColumnHeaders); rowIndex <= sheet.getLastRowNum(); rowIndex++) {

            Row row = sheet.getRow(rowIndex);

            if (row == null) {
                log.debug("Пропущена несуществующая строка excel ({})", rowIndex);

                continue;
            }

            MappedRow<T> rowResult;
            try {

                rowResult = processRow(
                        row,
                        new ColumnHeaderBag(foundColumnHeaders)
                );
            } catch (BlankRowException e) {

                Throwable rootCause = ExceptionUtils.getRootCause(e);
                String eMessage = e.getMessage();

                String message = (rootCause != null)
                        ? eMessage + ": " + rootCause.getMessage()
                        : eMessage;

                log.debug("Маппинг остановлен на пустой строке excel ({}): {}", rowIndex, message);

                break;
            }

            RowStatus mappedStatus = rowResult.getStatus();
            switch (mappedStatus) {

                case SUCCESS:
                    successRowCount++;
                    rowResults.add(rowResult);
                    break;
                case FAILED:
                    failedRowCount++;
                    rowResults.add(rowResult);
                    break;
                case SKIPPED:
                    break;
                default:
                    throw new IllegalStateException("Unknown MappedStatus: " + mappedStatus);
            }
        }

        return new MappedResult<>(rowResults, successRowCount, failedRowCount);
    }

    private int composeStartRowIndex(Collection<ColumnHeaderReference> foundColumnHeaders) {

        int columnHeadersRowIndex = foundColumnHeaders
                .stream()
                .findAny()
                .orElseThrow() // never occurs because of ColumnHeadersNotFoundException
                .getRowIndex();

        return columnHeadersRowIndex + 1;
    }

    /**
     * Обработка excel строки
     *
     * @param row             строка excel
     * @param columnHeaderBag контейнер найденных колонок по заголовку
     * @return результат импорта excel строки
     */
    private MappedRow<T> processRow(Row row, ColumnHeaderBag columnHeaderBag) throws ExcelMapperException { // TODO detail throws

        int rowIndex = row.getRowNum();

        try {

            T data = readRowData(row, columnHeaderBag);

            if (data == null) {

                throw new BlankRowException(rowIndex);
            }

            return handleSuccessRow(rowIndex, data);
        } catch (BlankRowException e) {

            if (haltOnBlankRow) {

                throw e;
            }

            return handleSkippedRow(rowIndex, e);
        } catch (RequiredColumnMissedException e) {

            if (terminateOnRequiredIsMissed) {

                throw e;
            }

            if (haltOnBlankRow) {

                throw new BlankRowException(rowIndex, e);
            }

            return handleSkippedRow(rowIndex, e);
        } catch (Exception e) {

            if (terminateOnError) {

                throw e;
            }

            return handleFailedRow(rowIndex, e);
        }

    }

    private T readRowData(Row row, ColumnHeaderBag columnHeaderBag)
            throws InvalidCellFormatException, RequiredColumnMissedException {

        return excelMappingDriver.readData(row, columnHeaderBag);
    }

    private MappedRow<T> handleSuccessRow(int rowIndex, T data) {

        log.debug("Строка успешно импортирована ({}): {}", rowIndex, data);

        return new MappedRow<>(rowIndex, RowStatus.SUCCESS, data, null);
    }

    private MappedRow<T> handleSkippedRow(int rowIndex, Exception e) {

        log.warn("Пропущена строка ({}): {}", rowIndex, e.getMessage());

        return new MappedRow<>(rowIndex, RowStatus.SKIPPED, null, e);
    }

    private MappedRow<T> handleFailedRow(int rowIndex, Exception e) {

        log.error("Ошибка при импорте строки ({}): ", rowIndex, e);

        return new MappedRow<>(rowIndex, RowStatus.FAILED, null, e);
    }

}
