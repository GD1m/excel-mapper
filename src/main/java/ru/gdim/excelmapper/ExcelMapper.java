package ru.gdim.excelmapper;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gdim.excelmapper.excel.column.ColumnHeaderBag;
import ru.gdim.excelmapper.excel.column.ExcelColumn;
import ru.gdim.excelmapper.excel.column.header.ColumnHeaderReference;
import ru.gdim.excelmapper.excel.column.header.provider.ColumnHeaderProvider;
import ru.gdim.excelmapper.excel.column.header.provider.FirstRowColumnHeaderProvider;
import ru.gdim.excelmapper.excel.row.RowResult;
import ru.gdim.excelmapper.excel.row.RowResultStatus;
import ru.gdim.excelmapper.excel.workbook.WorkbookFactory;
import ru.gdim.excelmapper.excel.workbook.XSSFWorkbookFactory;
import ru.gdim.excelmapper.exception.*;
import ru.gdim.excelmapper.mapper.ExcelMapperOptions;
import ru.gdim.excelmapper.mapper.MappedResult;
import ru.gdim.excelmapper.mapper.MappedStatistic;
import ru.gdim.excelmapper.mapper.driver.ExcelMappingDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Мапинг данных из excel
 *
 * @param <T> тип объекта с импортированными данными
 */
public class ExcelMapper<T> {

    private static final Logger log = LoggerFactory.getLogger(ExcelMapper.class);

    private final ExcelMappingDriver<T> excelMappingDriver;
    private final WorkbookFactory workbookFactory;
    private final ColumnHeaderProvider columnHeaderProvider;
    private final ExcelMapperOptions options;

    /**
     * Constructor with default configuration
     *
     * @param excelMappingDriver драйвер мапинга данных из строки excel
     */
    public ExcelMapper(ExcelMappingDriver<T> excelMappingDriver) {

        this(
                excelMappingDriver,
                new XSSFWorkbookFactory(),
                new FirstRowColumnHeaderProvider(),
                new ExcelMapperOptions()
        );
    }

    public ExcelMapper(
            ExcelMappingDriver<T> excelMappingDriver,
            WorkbookFactory workbookFactory,
            ColumnHeaderProvider columnHeaderProvider,
            ExcelMapperOptions options
    ) {

        this.excelMappingDriver = Objects.requireNonNull(excelMappingDriver);
        this.workbookFactory = Objects.requireNonNull(workbookFactory);
        this.columnHeaderProvider = Objects.requireNonNull(columnHeaderProvider);
        this.options = Objects.requireNonNull(options);
    }

    public ExcelMapperOptions options() {

        return options;
    }

    /**
     * Обработка excel файла
     *
     * @param path путь excel файла
     * @return результат мапинга
     */
    public MappedResult<T> read(String path) throws ExcelMapperException, IOException, InvalidFormatException {

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
    public MappedResult<T> read(File file) throws ExcelMapperException, IOException, InvalidFormatException {

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
    public MappedResult<T> read(InputStream inputStream) throws ExcelMapperException, IOException {

        return read(inputStream, 0);
    }

    public MappedResult<T> read(InputStream inputStream, int sheetIndex) throws ExcelMapperException, IOException {

        return processWorkBook(
                createWorkBook(inputStream),
                sheetIndex
        );
    }

    public MappedResult<T> read(InputStream inputStream, String sheetName) throws ExcelMapperException, IOException {

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
    private MappedResult<T> processWorkBook(Workbook workBook, int sheetIndex)
            throws SheetNotFoundException,
            ExcelColumnsNotSpecifiedException,
            ColumnHeadersNotFoundException,
            RequiredColumnMissedException,
            RowProcessingException,
            DuplicateSpecifiedColumnsException,
            DuplicateColumnHeadersException {

        Sheet sheet = workBook.getSheetAt(sheetIndex);

        if (sheet == null) {

            throw SheetNotFoundException.byIndex(sheetIndex);
        }

        return processSheet(sheet);
    }

    private MappedResult<T> processWorkBook(Workbook workBook, String sheetName)
            throws SheetNotFoundException,
            ExcelColumnsNotSpecifiedException,
            ColumnHeadersNotFoundException,
            RowProcessingException,
            RequiredColumnMissedException,
            DuplicateSpecifiedColumnsException,
            DuplicateColumnHeadersException {

        Sheet sheet = workBook.getSheet(sheetName);

        if (sheet == null) {

            throw SheetNotFoundException.byName(sheetName);
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
    private MappedResult<T> processSheet(Sheet sheet)
            throws ExcelColumnsNotSpecifiedException,
            ColumnHeadersNotFoundException,
            RequiredColumnMissedException,
            RowProcessingException,
            DuplicateSpecifiedColumnsException,
            DuplicateColumnHeadersException {

        log.debug("Маппинг листа excel '{}' начат...", sheet.getSheetName());
        log.debug("Options: {}", options);

        Collection<ExcelColumn> columns = excelMappingDriver.getExpectedColumns();

        log.debug("Specified columns: {}", columns);

        if (columns == null || columns.isEmpty()) {

            throw new ExcelColumnsNotSpecifiedException();
        }

        checkSpecifiedColumnsForDuplicates(columns);

        Collection<ColumnHeaderReference> foundColumnHeaders = columnHeaderProvider.getColumnHeaders(sheet, columns);

        log.debug("Found column headers by specified columns: {}", foundColumnHeaders);

        if (foundColumnHeaders == null || foundColumnHeaders.isEmpty()) {

            throw new ColumnHeadersNotFoundException();
        }

        checkColumnHeadersForDuplicates(foundColumnHeaders);

        MappedResult<T> result = processRows(sheet, foundColumnHeaders);

        log.debug("Маппинг листа excel завершен: {}", result.getStatistic());

        return result;
    }

    private void checkSpecifiedColumnsForDuplicates(Collection<ExcelColumn> columns) throws DuplicateSpecifiedColumnsException {

        Set<String> headerTitles = new HashSet<>();

        List<String> duplicates = columns
                .stream()
                .map(ExcelColumn::getHeaderTitle)
                .filter(title -> !headerTitles.add(title))
                .collect(Collectors.toList());

        if (!duplicates.isEmpty()) {

            throw new DuplicateSpecifiedColumnsException(duplicates);
        }
    }

    private void checkColumnHeadersForDuplicates(Collection<ColumnHeaderReference> foundColumnHeaders)
            throws DuplicateColumnHeadersException {

        Set<ExcelColumn> columns = new HashSet<>();

        List<String> duplicates = foundColumnHeaders
                .stream()
                .map(ColumnHeaderReference::getColumn)
                .filter(columnHeader -> !columns.add(columnHeader))
                .map(ExcelColumn::getHeaderTitle)
                .collect(Collectors.toList());

        if (!duplicates.isEmpty()) {

            throw new DuplicateColumnHeadersException(duplicates);
        }
    }

    private MappedResult<T> processRows(Sheet sheet, Collection<ColumnHeaderReference> foundColumnHeaders)
            throws RequiredColumnMissedException, RowProcessingException {

        List<RowResult<T>> rowResults = new ArrayList<>();
        MappedStatistic statistic = new MappedStatistic();

        for (int rowIndex = composeStartRowIndex(foundColumnHeaders); rowIndex <= sheet.getLastRowNum(); rowIndex++) {

            RowResult<T> rowResult = processRow(
                    sheet,
                    rowIndex,
                    new ColumnHeaderBag(foundColumnHeaders)
            );

            RowResultStatus rowResultStatus = rowResult.getStatus();

            if (
                    options.isHaltOnBlankRow() && (
                            rowResultStatus == RowResultStatus.BLANK
                                    || rowResultStatus == RowResultStatus.REQUIRED_COLUMN_MISSED
                    )
            ) {

                log.debug("Маппинг остановлен на строке с индексом {}: {}", rowIndex, rowResult);

                break;
            }

            rowResults.add(rowResult);
            updateStatistic(statistic, rowResultStatus); // TODO do not count skipped rows ?
        }

        return new MappedResult<>(rowResults, statistic);
    }

    private int composeStartRowIndex(Collection<ColumnHeaderReference> foundColumnHeaders) {

        int columnHeadersRowIndex = foundColumnHeaders
                .stream()
                .findAny()
                .orElseThrow(IllegalStateException::new) // never occurs because of ColumnHeadersNotFoundException
                .getRowIndex();

        return columnHeadersRowIndex + 1;
    }

    /**
     * Обработка excel строки
     *
     * @param sheet           лист excel
     * @param rowIndex        индекс строки excel
     * @param columnHeaderBag контейнер найденных колонок по заголовкам
     * @return результат импорта excel строки
     */
    private RowResult<T> processRow(Sheet sheet, int rowIndex, ColumnHeaderBag columnHeaderBag)
            throws RequiredColumnMissedException, RowProcessingException {

        Row row = sheet.getRow(rowIndex);

        if (row == null) {

            return handleBlankRow(rowIndex);
        }

        try {

            T data;

            try {

                data = readRowData(row, columnHeaderBag);
            } catch (RuntimeException e) {

                throw new RowProcessingException(row, e);
            }

            if (data == null) {

                return handleBlankRow(rowIndex);
            }

            return handleSuccessRow(rowIndex, data);
        } catch (RequiredColumnMissedException e) {

            return handleRequiredColumnMissedRow(rowIndex, e);
        } catch (RowProcessingException e) {

            return handleFailedRow(rowIndex, e);
        }

    }

    private T readRowData(Row row, ColumnHeaderBag columnHeaderBag)
            throws RowProcessingException, RequiredColumnMissedException {

        return excelMappingDriver.readData(row, columnHeaderBag);
    }

    private RowResult<T> handleBlankRow(int rowIndex) {

        log.debug("Пустая строка ({})", rowIndex);

        return new RowResult<>(rowIndex, RowResultStatus.BLANK, null, null);
    }

    private RowResult<T> handleSuccessRow(int rowIndex, T data) {

        log.debug("Строка успешно импортирована ({}): {}", rowIndex, data);

        return new RowResult<>(rowIndex, RowResultStatus.SUCCESS, data, null);
    }

    private RowResult<T> handleRequiredColumnMissedRow(int rowIndex, RequiredColumnMissedException e)
            throws RequiredColumnMissedException {

        log.warn("Не найдена обязательная колонка ({}): {}", rowIndex, e.getMessage());

        if (options.isFailOnRequiredColumnMissed()) {

            throw e;
        }

        return new RowResult<>(rowIndex, RowResultStatus.REQUIRED_COLUMN_MISSED, null, e);
    }

    private RowResult<T> handleFailedRow(int rowIndex, RowProcessingException e) throws RowProcessingException {

        log.error("Ошибка при импорте строки ({}): ", rowIndex, e);

        if (options.isFailOnError()) {

            throw e;
        }

        return new RowResult<>(rowIndex, RowResultStatus.FAILED, null, e);
    }

    private void updateStatistic(MappedStatistic statistic, RowResultStatus mappedStatus) {

        switch (mappedStatus) {

            case SUCCESS:
                statistic.setSuccessRowCount(statistic.getSuccessRowCount() + 1);
                break;

            case BLANK:
                statistic.setBlankRowCount(statistic.getBlankRowCount() + 1);
                break;

            case REQUIRED_COLUMN_MISSED:
                statistic.setRequiredColumnMissedRowCount(statistic.getRequiredColumnMissedRowCount() + 1);
                break;

            case FAILED:
                statistic.setFailedRowCount(statistic.getFailedRowCount() + 1);
                break;

            default:
                throw new IllegalStateException("Unknown MappedStatus: " + mappedStatus);
        }
    }

}
