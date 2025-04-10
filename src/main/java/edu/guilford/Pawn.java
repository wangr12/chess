package edu.guilford;

import javafx.scene.image.Image;

public class Pawn extends Piece {

    private boolean firstMove;
    private final Image pawnIcon;

    public Pawn(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        this.firstMove = true;
        if (color.equals("white")) {
            pawnIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wp.png").toExternalForm());
        } else {
            pawnIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bp.png").toExternalForm());
        }
        setIcon(this.pawnIcon);
    }

    // Method to check if the pawn can move to a new position
    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow, Piece[][] gamePieces) {
        int n = 1;
        if (this.color.equals("white")) {
            n = -1; // white pawns move down the board
        }

        if (newPositionColumn == positionColumn && newPositionRow == positionRow + n) {
            // check nothing is blocking the pawn
            if (gamePieces[newPositionColumn][newPositionRow] == null) {
                firstMove = false;
                return true;
            }
        }
        
        // move two squares forward
        else if (firstMove && newPositionColumn == positionColumn && newPositionRow == positionRow + 2*n) {
            // check nothing is blocking the pawn
            if ((gamePieces[positionColumn][positionRow + n] == null) && (gamePieces[positionColumn][positionRow + 2*n] == null)) {
                firstMove = false;
                return true;
            }
        }
        
        // capture diagonally
        else if (Math.abs(newPositionColumn - positionColumn) == 1 && newPositionRow == positionRow + n) {
            if  (gamePieces[newPositionColumn][newPositionRow] != null) {
                return checkCapture(gamePieces[newPositionColumn][newPositionRow]);
            }
        }
        
        return false; // invalid move
    }
}