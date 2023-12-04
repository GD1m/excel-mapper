package ru.gdim.excelmapper.exception;

import java.util.Collection;

public class DuplicateColumnHeadersException extends ExcelMapperException {

    public DuplicateColumnHeadersException(Collection<String> columnHeaders) {

        super("Duplicate column headers: " + columnHeaders);
    }

}
