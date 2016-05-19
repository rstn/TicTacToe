package com.simbirsoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Board cell.
 */
public class Cell {

    private int row;

    private int col;

    private CellType cellType;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        cellType = CellType.EMPTY;
    }

    /**
     * Reset the cell type.
     */
    public void clear() {
        cellType = CellType.EMPTY;
    }

    /**
     * Return true if the cell's type is EMPTY, false otherwise.
     * 
     * @return true if the cell's type is EMPTY, false otherwise.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return CellType.EMPTY.equals(cellType);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    @Override
    public String toString() {
        return getCellType().getValue();
    }
}
