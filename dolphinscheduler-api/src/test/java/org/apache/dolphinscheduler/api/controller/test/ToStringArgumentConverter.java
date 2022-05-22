package org.apache.dolphinscheduler.api.controller.test;

import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import static javolution.testing.TestContext.assertEquals;

public class ToStringArgumentConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType) {
        assertEquals(String.class, targetType, "Can only convert to String");
        if (source instanceof Enum<?>) {
            return ((Enum<?>) source).name();
        }
        return String.valueOf(source);
    }
}
