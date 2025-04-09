package edu.guilford;

import javafx.scene.image.Image;

public class Rook extends Piece {

    private Image rookIcon;

    public Rook(String color, int positionColumn, int positionRow) {
        super(color, positionColumn, positionRow);
        if (color.equals("white")) {
            rookIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/wr.png").toExternalForm());
        } else {
            rookIcon = new Image(this.getClass().getResource("/edu/guilford/pieces/br.png").toExternalForm());
        }
        setIcon(rookIcon);
    }

    @Override
    public boolean isValidMove(int newPositionColumn, int newPositionRow) {
        return (newPositionColumn == getPositionColumn() || newPositionRow == getPositionRow());
    }

}
