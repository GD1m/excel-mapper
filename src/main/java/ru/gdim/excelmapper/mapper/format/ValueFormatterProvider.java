package ru.gdim.excelmapper.mapper.format;

import ru.gdim.excelmapper.exception.ValueFormatterNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@SuppressWarnings("unused")
public final class ValueFormatterProvider {

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

    @SuppressWarnings("unchecked")
    public <T> ValueFormatter<T> getFormatterForType(Class<T> valueType) throws ValueFormatterNotFoundException {

        return (ValueFormatter<T>) valueFormatters
                .stream()
                .filter(formatter -> formatter.type().isAssignableFrom(valueType))
                .findFirst()
                .orElseThrow(() -> new ValueFormatterNotFoundException(valueType));
    }

    public ValueFormatter<?> getFormatterOfType(Class<?> valueFormatterType) throws ValueFormatterNotFoundException {

        return valueFormatters
                .stream()
                .filter(formatter -> Objects.equals(formatter.getClass(), valueFormatterType))
                .findFirst()
                .orElseThrow(() -> new ValueFormatterNotFoundException(valueFormatterType));
    }

    public void useDefaultFormatters() {

        valueFormatters = new HashSet<>();

        valueFormatters.add(new LongFormatter());
        valueFormatters.add(new BigDecimalFormatter());
        valueFormatters.add(new LocalDateFormatter());
        valueFormatters.add(new StringFormatter());
    }

}
