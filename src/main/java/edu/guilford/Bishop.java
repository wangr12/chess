package edu.guilford;

import javafx.scene.image.Image;

/**
 * The Bishop class represents a bishop piece in a chess game.
 * It extends the Piece class.
 */
public class Bishop extends Piece {

    private Image bishopIcon;

    /**
     * Constructor for the Bishop class.
     *
     * @param color          The color of the bishop (white or black).
     * @param positionColumn The column position of the bishop on the board.
     * @param positionRow    The row position of the bishop on the board.
     */
    public Bishop(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            bishopIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wb.png").toExternalForm());
        } else {
            bishopIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bb.png").toExternalForm());
        }
        setIcon(bishopIcon);
    }

    /**
     * Determines if the given move is valid for the bishop piece.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow, Piece[][] gamePieces) {
        int x = Math.abs(newPositionColumn - positionColumn);
        int y = Math.abs(newPositionRow - positionRow);
        if (x == y) { // is a diagonal move
            // check if there are any pieces in the way
            int xDirection = (newPositionColumn - positionColumn) / x;
            int yDirection = (newPositionRow - positionRow) / y;

            for (int i = 1; i < x; i++) {
                if (gamePieces[positionColumn + i * xDirection][positionRow + i * yDirection] != null) {
                    return false;
                }
            }
            // check if the selected square is empty or occupied by an opponent's piece
            if (gamePieces[newPositionColumn][newPositionRow] == null) {
                return true;
            } else {
                return checkCapture(gamePieces[newPositionColumn][newPositionRow]);
            }

        }
        return false;
    }

}
