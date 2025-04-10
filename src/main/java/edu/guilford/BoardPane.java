package edu.guilford;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BoardPane extends GridPane{

    private Piece[][] pieces; // 2D array to hold the pieces on the board
    private Button[][] squares;
    private int selectedRow;
    private int selectedCol;

    public BoardPane() {
        super();

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
                tempButton.setOnAction(event -> handleButtonClick(row, col));

                squares[i][j] = tempButton;
                this.add(tempButton, i, j);
            }
        }

        pieces = new Piece[8][8];
        setStartPos();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null) {
                    ImageView pieceImage = new ImageView(pieces[i][j].getIcon());
                    squares[i][j].setGraphic(pieceImage);
                }
            }
        }

        selectedRow = -1;
        selectedCol = -1;
        
    }

    public void setStartPos() {
        // pawns
        for (int i = 0; i < 8; i++) {
            pieces[i][6] = new Pawn("white", i, 6);
            pieces[i][1] = new Pawn("black", i, 1);
        }

        // rooks
        pieces[0][7] = new Rook("white", 0, 7);
        pieces[7][7] = new Rook("white", 7, 7);
        pieces[0][0] = new Rook("black", 0, 0);
        pieces[7][0] = new Rook("black", 7, 0);

        // knights
        pieces[1][7] = new Knight("white", 1, 7);
        pieces[6][7] = new Knight("white", 6, 7);
        pieces[1][0] = new Knight("black", 1, 0);
        pieces[6][0] = new Knight("black", 6, 0);

        // bishops
        pieces[2][7] = new Bishop("white", 2, 7);
        pieces[5][7] = new Bishop("white", 5, 7);
        pieces[2][0] = new Bishop("black", 2, 0);
        pieces[5][0] = new Bishop("black", 5, 0);

        // queens
        pieces[3][7] = new Queen("white", 3, 7);
        pieces[3][0] = new Queen("black", 3, 0);

        // kings
        pieces[4][7] = new King("white", 4, 7);
        pieces[4][0] = new King("black", 4, 0);

    }

    public void handleButtonClick(int i, int j) {
        // Handle the button click event here
        System.out.println("Button clicked at: " + i + ", " + j);
        if (pieces[i][j] != null) {
            if (selectedRow == -1) {
                squares[i][j].setStyle("-fx-background-color: red;");
                selectedRow = i;
                selectedCol = j;
            } else {
                checkMove(i, j);
            }
        }
        else {
            checkMove(i,j);
        }
    }

    public void checkMove(int i, int j) {
        // 3 options: valid square, invalid square, same square
        if (pieces[selectedRow][selectedCol].isValidMove(i, j, pieces)) {
            System.out.println("valid");
            // Move the piece
            pieces[i][j] = pieces[selectedRow][selectedCol];
            pieces[i][j].setPosition(i,j);
            pieces[selectedRow][selectedCol] = null;

            squares[i][j].setGraphic(new ImageView(pieces[i][j].getIcon()));
            squares[selectedRow][selectedCol].setGraphic(null);

            squares[selectedRow][selectedCol].setStyle("-fx-background-color: transparent;");
            selectedRow = -1;
            selectedCol = -1;
        } else {
            System.out.println("invalid");
            // invalid move or same square: reset selection
            squares[selectedRow][selectedCol].setStyle("-fx-background-color: transparent;");
            selectedRow = -1;
            selectedCol = -1;
        }
    }
}
