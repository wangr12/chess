package edu.guilford;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * This class represents the chess board and handles the layout of the squares and pieces.
 * It extends the GridPane class to create a grid layout for the chess board.
 * Each square on the board is represented by a Button, and the pieces are represented by ImageView objects.
 */
public class BoardPane extends GridPane{

    private Piece[][] pieces; // 2D array to hold the pieces on the board
    private Button[][] squares;
    private int selectedRow;
    private int selectedCol;

    /**
     * Constructor for the BoardPane class.
     * It initializes the chess board with 8x8 squares and sets up the pieces in their starting positions.
     */
    public BoardPane() {
        super();

        Game game = new Game();

        for (int i = 0; i < 8; i++) {
            ColumnConstraints col = new ColumnConstraints(100); // Each column is 100px wide
            RowConstraints row = new RowConstraints(100);       // Each row is 100px tall
            this.getColumnConstraints().add(col);
            this.getRowConstraints().add(row);
        }

        squares = new Button[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ImageView tempImage = new ImageView();
                Button tempButton = new Button();

                // setHalignment(tempSquare, HPos.CENTER);
                // setValignment(tempSquare, VPos.CENTER);

                tempButton.setMinSize(100, 100);
                tempButton.setMaxSize(100, 100);
                tempButton.setPrefSize(100, 100);
                tempButton.setStyle("-fx-background-color: transparent;");
                
                tempButton.setGraphic(tempImage);

                int row = i;
                int col = j;
                tempButton.setOnAction(event -> handleButtonClick(game, row, col));

                squares[i][j] = tempButton;
                this.add(tempButton, i, j);
            }
        }

        game.setSquares(squares);

        pieces = new Piece[8][8];
        game.setStartPos();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game.getPieces()[i][j] != null) {
                    ImageView pieceImage = new ImageView(game.getPieces()[i][j].getIcon());
                    game.getSquares()[i][j].setGraphic(pieceImage);
                }
            }
        }
        
    }

    /**
     * This method handles the button click events on the chess board.
     * It checks if a piece is selected and performs the appropriate action based on the game rules.
     *
     * @param game The current game instance.
     * @param i The column index of the clicked square.
     * @param j The row index of the clicked square.
     */
    public void handleButtonClick(Game game, int i, int j) {
        if (game.getPieces()[i][j] != null) {
            if (i == game.getSelectedCol() && j == game.getSelectedRow()) { // click same square --> deselect piece
                squares[game.getSelectedCol()][game.getSelectedRow()].setStyle("-fx-background-color: transparent;");

                if (game.getPieces()[i][j] instanceof King && game.getInCheck()) {
                    squares[game.getSelectedCol()][game.getSelectedRow()].setStyle("-fx-background-color: red;");
                }

                game.setSelectedCol(-1);
                game.setSelectedRow(-1);

            } else if (game.getTurn().equals(game.getPieces()[i][j].getColor())) {
                if (game.getSelectedRow() == -1) {
                    squares[i][j].setStyle("-fx-background-color: lightblue;");
                } else {
                    squares[game.getSelectedCol()][game.getSelectedRow()].setStyle("-fx-background-color: transparent;");
                    squares[i][j].setStyle("-fx-background-color: lightblue;");
                }
                game.setSelectedCol(i);
                game.setSelectedRow(j);
            } else {
                game.checkMove(i,j);
            }
        }
        else {
            if (game.getSelectedRow() != -1) {
                game.checkMove(i,j);
            }
        }
    }
}