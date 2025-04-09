package edu.guilford;

import javafx.scene.image.Image;

public class Bishop extends Piece {

    private Image bishopIcon;

    public Bishop(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            bishopIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wb.png").toExternalForm());
        } else {
            bishopIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bb.png").toExternalForm());
        }
        setIcon(bishopIcon);
    }

    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow) {
        return Math.abs(newPositionColumn - getPositionColumn()) == Math.abs(newPositionRow - getPositionRow());
    }

}
