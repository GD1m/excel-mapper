package ru.gdim.excelmapper.mapper.driver.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE, FIELD})
@Retention(RUNTIME)
public @interface Annotation {

    String columnName();

    boolean isRequired() default false;

}
