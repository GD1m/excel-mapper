package ru.gdim.excelmapper.integration.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.gdim.excelmapper.excel.column.ExcelColumn;

@RequiredArgsConstructor
@Getter
public enum SampleColumns implements ExcelColumn {

    LONG("Колонка long", true),
    BIG_DECIMAL("Колонка big decimal", false),
    DATE_AFTER_BLANK("Колонка после пропуска с датой", false);

    private final String headerTitle;
    private final boolean required;

}
