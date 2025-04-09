package edu.guilford;

import javafx.scene.image.Image;

public abstract class Piece {
    protected String color;
    protected int positionColumn;
    protected int positionRow;
    protected Image icon;
    
    public Piece(String color, int positionColumn, int positionRow) {
        this.color = color;
        this.positionColumn = positionColumn;
        this.positionRow = positionRow;
    }

    public String getColor() {
        return color;
    }
    public int getPositionColumn() {
        return positionColumn;
    }
    public int getPositionRow() {
        return positionRow;
    }
    public Image getIcon() {
        return icon;
    }
    public void setIcon(Image icon) {
        this.icon = icon;
    }
    public void setPosition(int positionColumn, int positionRow) {
        this.positionColumn = positionColumn;
        this.positionRow = positionRow;
    }

    public abstract boolean isValidMove(int positionColumn, int positionRow);
}
