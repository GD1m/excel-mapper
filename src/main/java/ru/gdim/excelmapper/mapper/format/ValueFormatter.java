package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;
import ru.gdim.excelmapper.exception.InvalidCellFormatException;

public interface ValueFormatter<T> {

    Class<T> type();

    T format(Cell cell) throws InvalidCellFormatException;

}








