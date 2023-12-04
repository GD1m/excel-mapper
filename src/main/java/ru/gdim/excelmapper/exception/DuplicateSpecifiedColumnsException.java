package ru.gdim.excelmapper.exception;

import java.util.Collection;

public class DuplicateSpecifiedColumnsException extends ExcelMapperException {

    public DuplicateSpecifiedColumnsException(Collection<String> columns) {

        super("Duplicate specified columns: " + columns);
    }

}
