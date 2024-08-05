package minesweeper;

import minesweeper.enums.MoveType;
import minesweeper.exceptions.AlreadyExploredCellException;
import minesweeper.exceptions.EndOfGameException;

import java.util.Scanner;

public class Game {
    Scanner scanner = new Scanner(System.in);

    public void play() {
        Board board = new Board(scanner);

        board.printBoard();

        boolean gameOver = false;
        boolean loss = false;

        while (!gameOver) {
            System.out.print("Set/unset mines marks or claim a cell as free: > ");
            int y = scanner.nextInt();
            int x = scanner.nextInt();

            MoveType typeOfMove;
            try {
                typeOfMove = MoveType.valueOf(scanner.next().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Incorrect move type. Please try again.");
                continue;
            }

            try {
                board.makeMove(x, y, typeOfMove);
            } catch (IllegalAccessException | AlreadyExploredCellException e) {
                System.out.println(e.getMessage());
                continue;
            } catch (EndOfGameException e) {
                gameOver = true;
                loss = true;
                board.setShowMines();
                board.printBoard();
                continue;
            }

            board.printBoard();

            gameOver = board.checkForWin();
        }

        if (loss) {
            System.out.println("You stepped on a mine and failed!");
        } else {
            System.out.println("Congratulations! You found all the mines!");
        }
    }
}

