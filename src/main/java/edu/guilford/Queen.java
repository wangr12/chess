package edu.guilford;

import javafx.scene.image.Image;

/**
 * The Queen class represents a queen piece in a chess game.
 * It extends the Piece class
 */
public class Queen extends Piece {

    private Image queenIcon;

    /**
     * Constructor for the Queen class.
     * @param color          The color of the queen (white or black).
     * @param positionColumn The column position of the queen on the board.
     * @param positionRow    The row position of the queen on the board.
     */
    public Queen(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            queenIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wq.png").toExternalForm());
        } else {
            queenIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bq.png").toExternalForm());
        }
        setIcon(queenIcon);
    }

    /**
     * Determines if the Queen can move to the specified position
     * @param newPositionColumn The column position to move to.
     * @param newPositionRow    The row position to move to.
     * @param gamePieces       The current state of the game board.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow, Piece[][] gamePieces) {
        if (Math.abs(newPositionColumn - positionColumn) == Math.abs(newPositionRow - positionRow) || 
            newPositionColumn == positionColumn || newPositionRow == positionRow) {
                
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
