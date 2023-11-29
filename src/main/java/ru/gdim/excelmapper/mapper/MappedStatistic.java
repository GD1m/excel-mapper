package ru.gdim.excelmapper.mapper;

import lombok.Data;

@Data
public final class MappedStatistic {

    /**
     * Количество успешно импортированных строк
     */
    private long successRowCount = 0;

    /**
     * Количество строк с ошибкой
     */
    private long failedRowCount = 0;

    private long blankRowCount = 0;

    private long requiredColumnMissedRowCount = 0;

}
