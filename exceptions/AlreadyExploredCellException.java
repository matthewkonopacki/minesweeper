package minesweeper.exceptions;

public class AlreadyExploredCellException extends Exception {
    public AlreadyExploredCellException() {
        super("This cell is already explored");
    }
}
