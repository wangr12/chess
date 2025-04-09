package edu.guilford;

import javafx.scene.image.Image;

public class Pawn extends Piece {

    private boolean firstMove;
    private Image pawnIcon;

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
    public boolean isValidMove(int newPositionColumn, int newPositionRow) {
        int n = 1;
        if (this.color.equals("black")) {
            n = -1; // Black pawns move down the board
        }

        if (newPositionColumn == positionColumn && newPositionRow == positionRow + 1*n) {
            return true; // Move one square forward

        } else if (firstMove && newPositionColumn == positionColumn && newPositionRow == positionRow + 2*n) {
            firstMove = false; // Update firstMove to false after the first move
            return true; // Move two squares forward

        } else if (Math.abs(newPositionColumn - positionColumn) == 1 && newPositionRow == positionRow + 1*n) {
            return true; // Capture diagonally
        }
        
        return false; // Invalid move
    }

}