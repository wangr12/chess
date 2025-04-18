package edu.guilford;

import javafx.scene.image.Image;

/**
 * The Rook class represents a bishop piece in a chess game.
 * It extends the Piece class
 */
public class Rook extends Piece {

    private Image rookIcon;
    private boolean hasMoved;

    /**
     * Constructor for the Rook class.
     * @param color The color of the rook (white or black).
     * @param positionColumn The column position of the rook on the chessboard.
     * @param positionRow The row position of the rook on the chessboard.
     */
    public Rook(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            rookIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wr.png").toExternalForm());
        } else {
            rookIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/br.png").toExternalForm());
        }
        setIcon(rookIcon);
        hasMoved = false;
    }

    /**
     * Returns whether the rook has moved (for castling).
     * @return
     */
    public boolean getHasMoved() {
        return hasMoved;
    }
    /**
     * Sets whether the rook has moved (for castling).
     * @param hasMoved
     */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Deterines if the rook can move to the specified position.
     * @param newPositionColumn The column position to move to.
     * @param newPositionRow The row position to move to.
     * @param gamePieces The current state of the chessboard.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow, Piece[][] gamePieces) {
        if (newPositionColumn == positionColumn || newPositionRow == positionRow) {
            // Check if there are any pieces in the way
            int xDirection = Integer.signum(newPositionColumn - positionColumn);
            int yDirection = Integer.signum(newPositionRow - positionRow);

            for (int i = 1; i < Math.max(Math.abs(newPositionColumn - positionColumn), Math.abs(newPositionRow - positionRow)); i++) {
                if (gamePieces[positionColumn + i * xDirection][positionRow + i * yDirection] != null) {
                    return false;
                }
            }
            
            // Check if the selected square is empty or occupied by an opponent's piece
            if (gamePieces[newPositionColumn][newPositionRow] == null) {
                return true;
            } else {
                return checkCapture(gamePieces[newPositionColumn][newPositionRow]);
            }
        }
        return false;
    }

}
