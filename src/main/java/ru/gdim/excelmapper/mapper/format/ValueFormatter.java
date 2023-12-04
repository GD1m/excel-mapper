package ru.gdim.excelmapper.mapper.format;

import org.apache.poi.ss.usermodel.Cell;

public interface ValueFormatter<T> {

    Class<T> valueType();

    T format(Cell cell);

}








