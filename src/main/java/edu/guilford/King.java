package edu.guilford;

import javafx.scene.image.Image;

public class King extends Piece {

    private Image kingIcon;
    private boolean canCastleQSide;
    private boolean canCastleKSide;

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

    public boolean getCanCastleQSide () {
        return canCastleQSide;
    }
    public void setCanCastleQSide (boolean canCastleQSide) {
        this.canCastleQSide = canCastleQSide;
    }
    public boolean getCanCastleKSide () {
        return canCastleKSide;
    }
    public void setCanCastleKSide (boolean canCastleKSide) {
        this.canCastleKSide = canCastleKSide;
    }

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
