package com.example;

import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** JavaFX application for the Tic-Tac-Toe game. */
public class TicTacToeFX extends Application {
    private static final String BUTTON_STYLE = "-fx-font-size: 30px;";
    private static final String WINNING_BUTTON_STYLE =
            "-fx-background-color: lightgreen; -fx-font-weight: bold;";

    private final Button[][] squareButtons = new Button[3][3];
    private Board board;
    private HumanPlayer humanPlayer;
    private CpuPlayer cpuPlayer;
    private Label statusLabel;
    private Label scoreLabel;
    private int humanWins;
    private int cpuWins;
    private int draws;
    private boolean gameOver;

    /** Creates the game window and initializes its controls. */
    @Override
    public void start(Stage stage) {
        board = new Board();
        humanPlayer = new HumanPlayer('X', new Scanner(System.in));
        cpuPlayer = new CpuPlayer('O');
        statusLabel = new Label();
        scoreLabel = new Label();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(16));

        statusLabel.setAlignment(Pos.CENTER);
        scoreLabel.setAlignment(Pos.CENTER);
        VBox top = new VBox(8, statusLabel, scoreLabel);
        top.setAlignment(Pos.CENTER);
        BorderPane.setMargin(top, new Insets(0, 0, 16, 0));
        root.setTop(top);
        updateScoreLabel();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(8);
        grid.setVgap(8);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                int buttonRow = row;
                int buttonColumn = column;
                Button square = new Button();
                square.setPrefSize(100, 100);
                square.setStyle(BUTTON_STYLE);
                square.setOnAction(event -> makeHumanMove(buttonRow, buttonColumn));
                squareButtons[row][column] = square;
                grid.add(square, column, row);
            }
        }
        root.setCenter(grid);

        Button newGameButton = new Button("New Game");
        newGameButton.setOnAction(event -> resetGame());
        HBox bottom = new HBox(newGameButton);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(16, 0, 0, 0));
        root.setBottom(bottom);

        resetGame();
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(new Scene(root, 400, 460));
        stage.show();
    }

    private void makeHumanMove(int row, int column) {
        if (gameOver || !humanPlayer.makeMove(board, row, column)) {
            return;
        }

        updateSquareButton(row, column);
        if (finishGameIfNeeded(humanPlayer)) {
            return;
        }

        cpuPlayer.makeMove(board);
        updateSquareButtons();
        if (!finishGameIfNeeded(cpuPlayer)) {
            updateTurnStatus();
        }
    }

    private void updateSquareButton(int row, int column) {
        Button button = squareButtons[row][column];
        button.setText(String.valueOf(board.getCell(row, column)));
        button.setDisable(true);
    }

    private void updateSquareButtons() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (!board.isValid(row, column)) {
                    updateSquareButton(row, column);
                }
            }
        }
    }

    private boolean finishGameIfNeeded(Player player) {
        if (board.checkWin()) {
            gameOver = true;
            if (player == humanPlayer) {
                humanWins++;
            } else {
                cpuWins++;
            }
            updateScoreLabel();
            highlightWinningCells(player.getMarker());
            statusLabel.setText(formatPlayer(player) + " wins!");
            disableEmptySquares();
            return true;
        }
        if (board.isFull()) {
            gameOver = true;
            draws++;
            updateScoreLabel();
            statusLabel.setText("Draw!");
            return true;
        }
        return false;
    }

    private void disableEmptySquares() {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                squareButtons[row][column].setDisable(true);
            }
        }
    }

    private void highlightWinningCells(char marker) {
        for (int index = 0; index < 3; index++) {
            if (isWinningLine(marker, index, 0, index, 1, index, 2)) {
                return;
            }
            if (isWinningLine(marker, 0, index, 1, index, 2, index)) {
                return;
            }
        }
        if (isWinningLine(marker, 0, 0, 1, 1, 2, 2)) {
            return;
        }
        isWinningLine(marker, 0, 2, 1, 1, 2, 0);
    }

    private boolean isWinningLine(char marker, int row1, int column1,
            int row2, int column2, int row3, int column3) {
        if (board.getCell(row1, column1) != marker
                || board.getCell(row2, column2) != marker
                || board.getCell(row3, column3) != marker) {
            return false;
        }
        squareButtons[row1][column1].setStyle(WINNING_BUTTON_STYLE);
        squareButtons[row2][column2].setStyle(WINNING_BUTTON_STYLE);
        squareButtons[row3][column3].setStyle(WINNING_BUTTON_STYLE);
        return true;
    }

    private void updateTurnStatus() {
        statusLabel.setText(formatPlayer(humanPlayer) + "'s turn");
    }

    private String formatPlayer(Player player) {
        return player.getName() + " (" + player.getMarker() + ")";
    }

    private void resetGame() {
        board.initialize();
        gameOver = false;
        for (Button[] row : squareButtons) {
            for (Button square : row) {
                if (square != null) {
                    square.setText("");
                    square.setDisable(false);
                    square.setStyle(BUTTON_STYLE);
                }
            }
        }
        updateTurnStatus();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + humanPlayer.getName() + ": " + humanWins
                + " | " + cpuPlayer.getName() + ": " + cpuWins + " | Draws: " + draws);
    }
}
