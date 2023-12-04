package ru.gdim.excelmapper.integration.custom;

import ru.gdim.excelmapper.excel.column.ExcelColumn;

import java.util.StringJoiner;

public enum SampleColumns implements ExcelColumn {

    INT_COLUMN("Int column", true),
    FLOAT_COLUMN("Float column", false),
    BIG_DECIMAL_COLUMN("Big Decimal column", false);

    private final String headerTitle;
    private final boolean required;

    SampleColumns(String headerTitle, boolean required) {

        this.headerTitle = headerTitle;
        this.required = required;
    }

    @Override
    public String getHeaderTitle() {

        return headerTitle;
    }

    @Override
    public boolean isRequired() {

        return required;
    }

    @Override
    public String toString() {

        return new StringJoiner(", ", SampleColumns.class.getSimpleName() + "[", "]")
                .add("headerTitle='" + headerTitle + "'")
                .add("required=" + required)
                .toString();
    }

}
