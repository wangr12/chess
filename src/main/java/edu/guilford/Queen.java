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
