package edu.guilford;

import javafx.scene.image.Image;

public class Knight extends Piece{

    private Image knightIcon;

    public Knight(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            knightIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wn.png").toExternalForm());
        } else {
            knightIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/bn.png").toExternalForm());
        }
        setIcon(knightIcon);
    }

    public boolean isValidMove(int newPositionColumn, int newPositionRow) {
        int columnDiff = Math.abs(newPositionColumn - getPositionColumn());
        int rowDiff = Math.abs(newPositionRow - getPositionRow());

        return (columnDiff == 2 && rowDiff == 1) || (columnDiff == 1 && rowDiff == 2);
    }

}
