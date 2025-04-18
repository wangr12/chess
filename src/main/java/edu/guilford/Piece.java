package edu.guilford;

import javafx.scene.image.Image;

/**
 * The Piece class is an abstract class that represents a chess piece on the board.
 * It contains the color, position, and icon of the piece.
 */
public abstract class Piece {
    protected String color;
    protected int positionColumn;
    protected int positionRow;
    protected Image icon;
    
    /**
     * Constructor for the Piece class.
     * @param color The color of the piece (e.g., "white" or "black").
     * @param positionColumn The column position of the piece on the board.
     * @param positionRow The row position of the piece on the board.
     */
    public Piece(String color, int positionColumn, int positionRow) {
        this.color = color;
        this.positionColumn = positionColumn;
        this.positionRow = positionRow;
    }

    /**
     * Gets the color of the piece
     * @return The color of the piece.
     */
    public String getColor() {
        return color;
    }
    /**
     * gets the column position of the piece
     * @return The column position of the piece.
     */
    public int getPositionColumn() {
        return positionColumn;
    }
    /**
     * gets the row position of the piece
     * @return The row position of the piece.
     */
    public int getPositionRow() {
        return positionRow;
    }
    /**
     * gets the icon of the piece
     * @return The icon of the piece.
     */
    public Image getIcon() {
        return icon;
    }
    /**
     * sets the color of the piece
     * @param color The color of the piece.
     */
    public void setIcon(Image icon) {
        this.icon = icon;
    }
    /**
     * sets the position of the piece
     * @param positionColumn The column position of the piece.
     * @param positionRow The row position of the piece.
     */
    public void setPosition(int positionColumn, int positionRow) {
        this.positionColumn = positionColumn;
        this.positionRow = positionRow;
    }

    /**
     * Checks if the piece can capture another piece.
     * @param targetPiece The piece to be captured.
     * @return true if the piece can capture the target piece, false otherwise.
     */
    public boolean checkCapture(Piece targetPiece) {
        if (targetPiece != null && !this.color.equals(targetPiece.getColor())) {
            return true;
        }
        return false;
    }

    /**
     * Abstract method to check if the move is valid for the specific piece.
     * @param positionColumn The column position to move to.
     * @param positionRow The row position to move to.
     * @param pieces The current state of the board.
     * @return true if the move is valid, false otherwise.
     */
    public abstract boolean isValidMove(int positionColumn, int positionRow, Piece[][] pieces);
}
