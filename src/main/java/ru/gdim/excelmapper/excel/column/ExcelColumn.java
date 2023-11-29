package ru.gdim.excelmapper.excel.column;

/**
 * Представление excel колонки (Искомая колонка)
 */
public interface ExcelColumn {

    /**
     * @return заголовок колонки
     */
    String getHeaderTitle();

    /**
     * @return является ли колонка обязательной
     */
    boolean isRequired();

}
