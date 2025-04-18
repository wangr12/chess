package edu.guilford;

import javafx.scene.image.Image;

/**
 * The King class represents a king piece in a chess game.
 * It extends the Piece class.
 */
public class King extends Piece {

    private Image kingIcon;
    private boolean canCastleQSide;
    private boolean canCastleKSide;

    /**
     * Constructor for the King class.
     * @param color The color of the king (white or black).
     * @param positionColumn The column position of the king on the board.
     * @param positionRow The row position of the king on the board.
     */
    public King(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            kingIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wk.png").toExternalForm());
        } else {
            kingIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bk.png").toExternalForm());
        }
        setIcon(kingIcon);

        canCastleQSide = true;
        canCastleKSide = true;
    }

    /**
     * Gets getCanCastleQSide.
     * @return true if the king can castle queenside, false otherwise.
     */
    public boolean getCanCastleQSide () {
        return canCastleQSide;
    }
    /**
     * Sets canCastleQSide.
     * @param canCastleQSide true if the king can castle queenside, false otherwise.
     */
    public void setCanCastleQSide (boolean canCastleQSide) {
        this.canCastleQSide = canCastleQSide;
    }
    /**
     * Gets getCanCastleKSide.
     * @return true if the king can castle kingside, false otherwise.
     */
    public boolean getCanCastleKSide () {
        return canCastleKSide;
    }
    /**
     * Sets canCastleKSide.
     * @param canCastleKSide true if the king can castle kingside, false otherwise.
     */
    public void setCanCastleKSide (boolean canCastleKSide) {
        this.canCastleKSide = canCastleKSide;
    }

    /**
     * Checks if the move is valid for the king.
     * @param newPositionColumn The new column position of the king.
     * @param newPositionRow The new row position of the king.
     * @param gamePieces The current state of the game pieces on the board.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow, Piece[][] gamePieces) {
        int x = Math.abs(newPositionColumn - positionColumn);
        int y = Math.abs(newPositionRow - positionRow);
        
        if ((x == 1 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 1)) { // is a valid king move
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
