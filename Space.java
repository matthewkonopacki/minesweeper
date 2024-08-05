package minesweeper;

import minesweeper.exceptions.AlreadyExploredCellException;
import minesweeper.exceptions.EndOfGameException;

public class Space {
    protected String defaultValue = ".";
    protected int noOfSurroundingMines = 0;
    protected boolean mine = false;
    protected boolean isExplored = false;
    private boolean marked = false;

    public String getDisplayValue(boolean showMine) {
        if (isExplored && noOfSurroundingMines == 0) {
            return "/";
        } else if (this.mine && showMine) {
            return "X";
        } else if (isExplored && noOfSurroundingMines > 0) {
            return String.valueOf(noOfSurroundingMines);
        } else if (marked) {
            return "*";
        } else {
            return defaultValue;
        }
    }

    public boolean getIsExplored() {
        return isExplored;
    }

    public void setIsExplored() throws EndOfGameException, AlreadyExploredCellException {
        if (mine) {
            throw new EndOfGameException("This space is a mine!");
        } else if (isExplored && noOfSurroundingMines > 0) {
            throw new AlreadyExploredCellException();
        }

        isExplored = true;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isMarked() {
        return this.marked;
    }

    public void setIsMarked() throws IllegalAccessException {
        this.marked = !this.marked;
    }

    public int getNoOfSurroundingMines() {
        return noOfSurroundingMines;
    }

    public void setNoOfSurroundingMines(int noOfSurroundingMines) {
        this.noOfSurroundingMines = noOfSurroundingMines;
    }
}
