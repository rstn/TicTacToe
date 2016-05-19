package com.simbirsoft.model;

public enum CellType {
    EMPTY(" "), CROSS("X"), NOUGHT("O");

    private final String value;

    CellType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
