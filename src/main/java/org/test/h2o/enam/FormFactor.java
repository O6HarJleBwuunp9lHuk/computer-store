package org.test.h2o.enam;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Форм-факторы настольных компьютеров.
 *
 * @author Bredikhin Andrey
 * @version 1.0
 */
public enum FormFactor {

    DESKTOP("DESKTOP"),
    NETTOP("NETTOP"),
    ALL_IN_ONE("ALL_IN_ONE");

    private final String value;

    FormFactor(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static FormFactor fromValue(String value) {
        for (FormFactor ff : FormFactor.values()) {
            if (ff.value.equalsIgnoreCase(value)) {
                return ff;
            }
        }
        throw new IllegalArgumentException("Unknown form factor: " + value);
    }
}