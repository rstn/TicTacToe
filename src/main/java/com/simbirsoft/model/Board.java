package com.simbirsoft.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Game board.
 */
public class Board {

    private static final int SIZE = 3;

    private Cell[][] cells;

    public Board() {
        init();
    }

    public Cell[][] getBoard() {
        return cells;
    }

    private void init() {
        cells = new Cell[SIZE][SIZE];
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    /**
     * Clear the board.
     */
    public void clear() {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                cells[row][col].clear();
            }
        }
    }

    private boolean isCorrectMove(Point point) {
        return point.getRow() >= 0 && point.getRow() < SIZE && point.getCol() >= 0 && point.getCol() < SIZE;
    }

    /**
     * Check availability of move.
     * 
     * @param point point on board
     * @return true if move is available, false otherwise.
     */
    public boolean canMove(Point point) {
        return isCorrectMove(point) && cells[point.getRow()][point.getCol()].isEmpty();
    }

    /**
     * Try to change cell on the board by player.
     * 
     * @param player what player move
     * @param point point on board
     */
    public void move(Player player, Point point) {
        if (canMove(point)) {
            CellType cellType = player.getPlayerType().getCellType();
            cells[point.getRow()][point.getCol()].setCellType(cellType);
        }
    }

    private boolean cellsEquals(List<Cell> cellList, CellType cellType) {
        return cellList.stream().allMatch(cell -> cell.getCellType().equals(cellType));
    }

    private boolean winOnRows(Player player) {
        CellType cellType = player.getPlayerType().getCellType();
        List<Cell> cellList = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            cellList.clear();
            for (int col = 0; col < SIZE; col++) {
                cellList.add(cells[row][col]);
            }
            if (cellsEquals(cellList, cellType)) {
                return true;
            }
        }
        return false;
    }

    private boolean winOnCols(Player player) {
        CellType cellType = player.getPlayerType().getCellType();
        List<Cell> cellList = new ArrayList<>();
        for (int col = 0; col < SIZE; col++) {
            cellList.clear();
            for (int row = 0; row < SIZE; row++) {
                cellList.add(cells[row][col]);
            }
            if (cellsEquals(cellList, cellType)) {
                return true;
            }
        }
        return false;
    }

    private boolean winOnDiagonals(Player player) {
        CellType cellType = player.getPlayerType().getCellType();
        List<Cell> firstDiagonalCells = new ArrayList<>();
        List<Cell> secondDiagonalCells = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            firstDiagonalCells.add(cells[row][row]);
            secondDiagonalCells.add(cells[row][SIZE - row - 1]);
        }
        return cellsEquals(firstDiagonalCells, cellType) || cellsEquals(secondDiagonalCells, cellType);
    }

    /**
     * Check win of player on rows, on columns or on diagonals.
     * 
     * @param player one of players
     * @return player wins?
     */
    public boolean hasWon(Player player) {
        return winOnRows(player) || winOnCols(player) || winOnDiagonals(player);
    }

    /**
     * The board has empty cells (draw)?
     * 
     * @return true - free cell exists, false - no free cell.
     */
    @JsonIgnore
    public boolean hasEmptyCell() {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                if (cells[row][col].isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}
