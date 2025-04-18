package edu.guilford;

import javafx.scene.image.Image;

/**
 * The Knight class represents a knight piece in a chess game.
 * It extends the Piece class.
 */
public class Knight extends Piece{

    private Image knightIcon;

    /**
     * Constructor for the Knight class.
     * @param color The color of the knight (white or black).
     * @param positionColumn The column position of the knight on the board.
     * @param positionRow The row position of the knight on the board.
     */
    public Knight(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            knightIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wn.png").toExternalForm());
        } else {
            knightIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bn.png").toExternalForm());
        }
        setIcon(knightIcon);
    }

    /**
     * Determines if the knight can move to the given position
     * @param newPositionColumn The column position to move to
     * @param newPositionRow The row position to move to
     * @param gamePieces The current state of the game board
     * @return true if the move is valid, false otherwise
     */
    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow, Piece[][] gamePieces) {
        int columnDiff = Math.abs(newPositionColumn - getPositionColumn());
        int rowDiff = Math.abs(newPositionRow - getPositionRow());
        if ((columnDiff == 2 && rowDiff == 1) || (columnDiff == 1 && rowDiff == 2)) {
            if (gamePieces[newPositionColumn][newPositionRow] == null) {
                return true;
            } else {
                return checkCapture(gamePieces[newPositionColumn][newPositionRow]);
            }
        } 
        return false;
    }

}
