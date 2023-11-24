package ru.gdim.excelmapper.mapper.driver.annotation;

import lombok.*;
import ru.gdim.excelmapper.excel.column.ExcelColumn;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public final class GenericExcelColumn implements ExcelColumn {

    private String title;
    private boolean required;
    private Class<?> valueType;

}
