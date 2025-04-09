package edu.guilford;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BoardPane extends GridPane{

    private Piece[][] pieces; // 2D array to hold the pieces on the board
    private ImageView[][] squares;

    public BoardPane() {
        super();

        for (int i = 0; i < 8; i++) {
            ColumnConstraints col = new ColumnConstraints(100); // Each column is 100px wide
            RowConstraints row = new RowConstraints(100);       // Each row is 100px tall
            this.getColumnConstraints().add(col);
            this.getRowConstraints().add(row);
        }

        squares = new ImageView[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ImageView tempSquare = new ImageView();
                squares[i][j] = tempSquare;
                tempSquare.setFitWidth(90);
                tempSquare.setFitHeight(90);
                setHalignment(tempSquare, HPos.CENTER);
                setValignment(tempSquare, VPos.CENTER);
                this.add(tempSquare, i, j);
            }
        }

        pieces = new Piece[8][8];
        setStartPos();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null) {
                    squares[i][j].setImage(pieces[i][j].getIcon());
                }
            }
        }

        
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
}
