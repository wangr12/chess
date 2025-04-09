package edu.guilford;

import javafx.scene.image.Image;

public class Queen extends Piece {

    private Image queenIcon;

    public Queen(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            queenIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wq.png").toExternalForm());
        } else {
            queenIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bq.png").toExternalForm());
        }
        setIcon(queenIcon);
    }

    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow) {
        return Math.abs(newPositionColumn - getPositionColumn()) == Math.abs(newPositionRow - getPositionRow())
                || newPositionColumn == getPositionColumn() || newPositionRow == getPositionRow();
    }

}
