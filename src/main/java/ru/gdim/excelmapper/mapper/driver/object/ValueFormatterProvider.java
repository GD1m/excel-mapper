package ru.gdim.excelmapper.mapper.driver.object;

import ru.gdim.excelmapper.exception.ValueFormatterNotFoundException;
import ru.gdim.excelmapper.mapper.format.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ValueFormatterProvider {

    private Collection<ValueFormatter<?>> valueFormatters;

    public ValueFormatterProvider() {

        useDefaultFormatters();
    }

    public ValueFormatterProvider(Collection<ValueFormatter<?>> valueFormatters) {

        this.valueFormatters = valueFormatters;
    }

    public Collection<ValueFormatter<?>> getValueFormatters() {

        return valueFormatters;
    }

    public void setValueFormatters(Collection<ValueFormatter<?>> valueFormatters) {

        this.valueFormatters = valueFormatters;
    }

    public void registerValueFormatter(ValueFormatter<?> valueFormatter) {

        valueFormatters.add(valueFormatter);
    }

    public void registerValueFormatters(Collection<ValueFormatter<?>> valueFormatters) {

        this.valueFormatters.addAll(valueFormatters);
    }

    public ValueFormatter<?> getFormatterOfType(Class<?> valueFormatterType) throws ValueFormatterNotFoundException {

        return valueFormatters
                .stream()
                .filter(formatter -> Objects.equals(formatter.getClass(), valueFormatterType))
                .findFirst()
                .orElseThrow(() -> new ValueFormatterNotFoundException(valueFormatterType));
    }

    @SuppressWarnings("unchecked")
    public <T> ValueFormatter<T> getFormatterForType(Class<T> valueType) throws ValueFormatterNotFoundException {

        return (ValueFormatter<T>) valueFormatters
                .stream()
                .filter(formatter -> formatter.valueType().isAssignableFrom(valueType))
                .findFirst()
                .orElseThrow(() -> new ValueFormatterNotFoundException(valueType));
    }

    public void useDefaultFormatters() { // TODO move it somewhere

        valueFormatters = new HashSet<>();

        valueFormatters.add(new BigDecimalFormatter());
        valueFormatters.add(new BooleanFormatter());
        valueFormatters.add(new DateFormatter());
        valueFormatters.add(new DoubleFormatter());
        valueFormatters.add(new FloatFormatter());
        valueFormatters.add(new LocalDateFormatter());
        valueFormatters.add(new LocalDateTimeFormatter());
        valueFormatters.add(new LongFormatter());
        valueFormatters.add(new StringFormatter());
    }

}
