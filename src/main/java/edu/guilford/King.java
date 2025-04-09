package edu.guilford;

import javafx.scene.image.Image;

public class King extends Piece {

    private Image kingIcon;

    public King(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            kingIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wk.png").toExternalForm());
        } else {
            kingIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bk.png").toExternalForm());
        }
        setIcon(kingIcon);
    }

    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow) {
        return (Math.abs(newPositionColumn - getPositionColumn()) <= 1 && Math.abs(newPositionRow - getPositionRow()) <= 1);
    }
}
