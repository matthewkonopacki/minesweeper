package minesweeper;

import minesweeper.enums.MoveType;
import minesweeper.exceptions.AlreadyExploredCellException;
import minesweeper.exceptions.EndOfGameException;

import java.util.*;
import java.util.stream.IntStream;

public class Board {
    Scanner scanner;
    Space[][] boardCopy = new Space[9][9];
    boolean showMines = false;
    int noOfMines;

    public Board(Scanner scanner) {
        this.scanner = scanner;
        int [] mineLocations = this.generateMineLocations();
        this.setupBoard(mineLocations);
    }


    public void printBoard() {
        System.out.println(" |123456789| ");
        System.out.println("-|---------|-");
        for (int row = 0; row < 9; row++) {
            String[] stringRow = new String[9];

            for (int col = 0; col < 9; col++) {
                stringRow[col] = this.boardCopy[row][col].getDisplayValue(this.showMines);
            }

            String rowString = (row + 1) +
                    "|" +
                    String.join("", stringRow) +
                    "| ";
            System.out.println(rowString);
        }
        System.out.println("-|---------|-");
    }

    private int[] generateMineLocations() {
        System.out.print("How many mines do you want on the field? > ");
        this.noOfMines = this.scanner.nextInt();

        int[] mines = new int[noOfMines];
        Random rand = new Random();

        int counter = 0;
        while (counter < mines.length) {
            int randomInt = rand.nextInt(81);

            boolean doesArrayContainInt = IntStream.of(mines).anyMatch(x -> x == randomInt);

            if (doesArrayContainInt) {
                continue;
            }

            mines[counter] = randomInt;
            counter++;
        }

        return mines;
    }

    private void setupBoard(int[] mineLocations) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boardCopy[i][j] = new Space();
            }
        }

        for (int i = 0; i < mineLocations.length; i++) {
            int row = mineLocations[i] / 9;
            int col = mineLocations[i] % 9;

            Mine mine = new Mine();

            this.boardCopy[row][col] = mine;
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int numberOfSurroundingMines = calculateSurroundingMines(row, col);

                if (!boardCopy[row][col].isMine()) {
                    boardCopy[row][col].setNoOfSurroundingMines(numberOfSurroundingMines);
                }
            }
        }
    }

    public int calculateSurroundingMines(int row, int col) {
        if (boardCopy[row][col].isMine()) {
            return 0;
        }

        int startRow = Math.max(row - 1, 0);
        int startCol = Math.max(col - 1, 0);
        int endRow = Math.min((row - 1) + 3, 9);
        int endCol = Math.min((col - 1) + 3, 9);

        int number = 0;

        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                if (boardCopy[i][j].isMine()) {
                    number++;
                }
            }
        }

        return number;
    }

    public void makeMove(int row, int col, MoveType moveType) throws AlreadyExploredCellException, IllegalAccessException, EndOfGameException {
        int xIndex = row - 1;
        int yIndex = col - 1;
        Space space = boardCopy[xIndex][yIndex];

        if (moveType == MoveType.MINE) {
            space.setIsMarked();
        } else if (moveType == MoveType.FREE) {
            space.setIsExplored();
            if (space.getNoOfSurroundingMines() == 0) {
                fillOpenAreas(xIndex, yIndex);
            }
        }


    }

    public boolean checkForWin() {
        int numberOfExplored = 0;
        int numberOfMarkedMines = 0;

        for (Space[] row : boardCopy) {
            for (Space space : row) {
                if (space.isMine() && space.isMarked()) {
                    numberOfMarkedMines++;
                }

                if (!space.isMine() && space.getIsExplored()) {
                    numberOfExplored++;
                }
            }
        }

        return numberOfMarkedMines == this.noOfMines || numberOfExplored == (81 - noOfMines);
    }

    public void fillOpenAreas(int x, int y) throws EndOfGameException, AlreadyExploredCellException {
        Deque<Coordinate> openAreas = new ArrayDeque<Coordinate>();

        openAreas.add(new Coordinate(x, y));
        Space space = this.boardCopy[x][y];
        space.setIsExplored();

        while (!openAreas.isEmpty()) {
            Coordinate coordinate = openAreas.poll();

            int row = coordinate.getRow();
            int col = coordinate.getCol();

            List<Coordinate> possibleCoordinates = new ArrayList<>();

            possibleCoordinates.add(new Coordinate(row - 1, col));
            possibleCoordinates.add(new Coordinate(row - 1, col - 1));
            possibleCoordinates.add(new Coordinate(row - 1, col + 1));
            possibleCoordinates.add(new Coordinate(row + 1, col));
            possibleCoordinates.add(new Coordinate(row + 1, col - 1));
            possibleCoordinates.add(new Coordinate(row + 1, col + 1));
            possibleCoordinates.add(new Coordinate(row, col - 1));
            possibleCoordinates.add(new Coordinate(row, col + 1));

            for (Coordinate possibleCoordinate : possibleCoordinates) {
                int possibleColumn = possibleCoordinate.getCol();
                int possibleRow = possibleCoordinate.getRow();

                if (possibleColumn < 0 || possibleRow < 0 || possibleColumn > 8 || possibleRow > 8) {
                    continue;
                }

                Space possibleSpace = boardCopy[possibleRow][possibleColumn];

                if (!possibleSpace.getIsExplored() && !possibleSpace.isMine()) {
                    possibleSpace.setIsExplored();


                    if (possibleSpace.getNoOfSurroundingMines() == 0) {
                        openAreas.add(possibleCoordinate);
                    }
                }
            }
        }
    }

    public void setShowMines() {
        this.showMines = true;
    }
}
