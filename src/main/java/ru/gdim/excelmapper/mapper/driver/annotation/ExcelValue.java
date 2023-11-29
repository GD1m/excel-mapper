package ru.gdim.excelmapper.mapper.driver.annotation;

import ru.gdim.excelmapper.mapper.format.ValueFormatter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface ExcelValue {

    String columnHeaderTitle();

    boolean isRequired() default false;

    Class<? extends ValueFormatter<?>>[] valueFormatter() default {};

}
