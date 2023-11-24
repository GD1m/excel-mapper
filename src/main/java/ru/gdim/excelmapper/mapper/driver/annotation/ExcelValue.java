package ru.gdim.excelmapper.mapper.driver.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface ExcelValue {

    String columnName();

    boolean isRequired() default false;

}
