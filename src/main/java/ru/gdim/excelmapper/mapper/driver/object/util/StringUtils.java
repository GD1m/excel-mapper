package ru.gdim.excelmapper.mapper.driver.object.util;

import java.util.Objects;

public class StringUtils {

    public static String capitalize(String string) {

        return Objects
                .requireNonNull(string)
                .substring(0, 1)
                .toUpperCase() + string.substring(1);
    }

}
