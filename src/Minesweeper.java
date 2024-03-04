

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private ArrayList<ArrayList<String>> board = new ArrayList<>();
    private ArrayList<String> coordinates = new ArrayList<>();
    private ArrayList<String> mineLocations = new ArrayList<>();
    private ArrayList<String> availableMineLocations = new ArrayList<>();
    private ArrayList<String> wrongGuesses = new ArrayList<>();
    private String mine = "X";
    private String safe = ".";
    private String guess = "*";
    private Scanner scanner = new Scanner(System.in);

    Minesweeper() {
        initializeBoard();
        setCoordinates();
        setMines();
        setHints();
        hideMines();
    }

    public void runGame () {
        do {
            printBoard();
            System.out.println("Set/delete mines marks (x and y coordinates):");
            String inputLine = scanner.nextLine();

            String[] coordinate = inputLine.split(" ");

            int index = Integer.parseInt(coordinate[0]) + 1;
            int row = Integer.parseInt(coordinate[1]) + 1;

            String value = board.get(row).get(index);

            if (value.equals(safe)) {
                board.get(row).set(index, guess);
                String guessCoordinate = String.format("%d-%d",row, index);

                if (mineLocations.contains(guessCoordinate)) {
                    availableMineLocations.remove(guessCoordinate);
                } else {
                    wrongGuesses.add(guessCoordinate);
                }
            } else if(value.equals(guess)) {
                board.get(row).set(index, safe);

                String guessCoordinate = String.format("%d-%d",row, index);
                if (mineLocations.contains(guessCoordinate)) {
                    availableMineLocations.add(guessCoordinate);
                } else {
                    wrongGuesses.remove(guessCoordinate);
                }
            } else {
                System.out.println("There is a number here!");
                continue;
            }

            if (availableMineLocations.isEmpty() && wrongGuesses.isEmpty()) {
                printBoard();
                System.out.println("Congratulations! You found all the mines!");
                break;
            }
        } while(true);
    }

    private void setCoordinates() {
        for (int i = 2; i <= 10; i++) {
            for (int j = 2; j <= 10; j++) {
                coordinates.add(String.format("%d-%d", i, j));
            }
        }
    }

    private void setMines() {
        System.out.println("How many mines do you want on the field?");
        int mines = Integer.parseInt(scanner.nextLine());
        Random random = new Random();


        for (int i = 0; i < mines; i++) {
            // Generate a random index within the range of coordinates
            int randomIndex = random.nextInt(coordinates.size());
            String[] coordinate = coordinates.get(randomIndex).split("-");
            int row = Integer.parseInt(coordinate[0]);
            int index = Integer.parseInt(coordinate[1]);

            board.get(row).set(index, mine);
            mineLocations.add(coordinates.get(randomIndex));
            availableMineLocations.add(coordinates.get(randomIndex));
            coordinates.remove(randomIndex);
        }
    }

    private void setHints() {
        for (int i = 2; i <= 10; i++) {
            for (int j = 2; j <= 10; j++) {
                if (hasMine(i, j)) {
                    continue;
                }
                int mines = countHowManyMinesAreAround(i, j);
                if (mines != 0) {
                    board.get(i).set(j, String.valueOf(mines));
                }
            }
        }
    }

    private int countHowManyMinesAreAround(int rowIndex, int cell) {
        int counter = 0;

        if (hasMineUpperDiagonalLeft(rowIndex, cell)) {
            counter++;
        }

        if (hasMineCellAbove(rowIndex, cell)) {
            counter++;
        }

        if (hasMineUpperDiagonalRight(rowIndex, cell)) {
            counter++;
        }

        if (hasMinePreviousCell(rowIndex, cell)) {
            counter++;
        }

        if (hasMineNextCell(rowIndex, cell)) {
            counter++;
        }

        if (hasMineLowerDiagonalLeft(rowIndex, cell)) {
            counter++;
        }

        if (hasMineCellUnder(rowIndex, cell)) {
            counter++;
        }

        if (hasMineLowerDiagonalRight(rowIndex, cell)) {
            counter++;
        }

        return counter;
    }

    private boolean hasMineUpperDiagonalLeft(int rowIndex, int cell) {
        return hasMine(rowIndex - 1, cell - 1);
    }

    private boolean hasMineCellAbove(int rowIndex, int cell) {
        return hasMine(rowIndex - 1, cell);
    }

    private boolean hasMineUpperDiagonalRight(int rowIndex, int cell) {
        return hasMine(rowIndex - 1, cell + 1);
    }

    private boolean hasMinePreviousCell(int rowIndex, int cell) {
        return hasMine(rowIndex, cell - 1);
    }

    private boolean hasMineNextCell(int rowIndex, int cell) {
        return hasMine(rowIndex, cell + 1);
    }

    private boolean hasMineLowerDiagonalLeft(int rowIndex, int cell) {
        return hasMine(rowIndex + 1, cell - 1);
    }

    private boolean hasMineCellUnder(int rowIndex, int cell) {
        return hasMine(rowIndex + 1, cell);
    }

    private boolean hasMineLowerDiagonalRight(int rowIndex, int cell) {
        return hasMine(rowIndex + 1, cell + 1);
    }

    private boolean hasMine(int rowIndex, int cell) {
        try {
            // Access an element at an index that may cause IndexOutOfBoundsException
            String value = board.get(rowIndex).get(cell);
            return value.equals(mine);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private void hideMines() {
        for (String location : mineLocations) {
            String[] coordinate = location.split("-");
            int row = Integer.parseInt(coordinate[0]);
            int index = Integer.parseInt(coordinate[1]);

            board.get(row).set(index, safe);
        }
    }

    private void initializeBoard() {
        board.add(new ArrayList<>(Arrays.asList(" ", "|", "1", "2", "3", "4", "5", "6", "7", "8", "9", "|")));
        board.add(new ArrayList<>(Arrays.asList("-", "|", "-", "-", "-", "-", "-", "-", "-", "-", "-", "|")));

        for (int i = 0; i < 9; i++) {
            board.add(new ArrayList<>(Arrays.asList(String.valueOf(i + 1), "|", safe, safe, safe, safe, safe, safe, safe, safe, safe, "|")));
        }

        board.add(new ArrayList<>(Arrays.asList("-", "|", "-", "-", "-", "-", "-", "-", "-", "-", "-", "|")));
    }

    private void printBoard() {
        for(ArrayList<String> row : board) {
            System.out.println(String.join("", row));
        }
    }

}
