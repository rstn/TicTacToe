package com.simbirsoft.model;

public enum PlayerType {
    CROSS, NOUGHT;

    public CellType getCellType() {
        return CROSS.equals(this) ? CellType.CROSS : CellType.NOUGHT;
    }
}
