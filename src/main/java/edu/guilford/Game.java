package edu.guilford;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Game {
    private Piece[][] pieces; // 2D array to hold the pieces on the board
    private Button[][] squares;
    private int selectedRow;
    private int selectedCol;

    private King whiteKing;
    private King blackKing;
    private King currentKing; // current turn's king

    private String turn;
    private boolean inCheck;

    private Piece[] whitePieces;
    private Piece[] blackPieces;
    private Piece[] currentPieces; // set of pieces for the current turn
    private Piece[] opponentPieces; // set of pieces for the current turn's opponent

    public Game() {
        this.pieces = new Piece[8][8];
        this.squares = new Button[8][8];
        this.selectedRow = -1;
        this.selectedCol = -1;
        this.turn = "white";
        this.inCheck = false;
        this.whitePieces = new Piece[16];
        this.blackPieces = new Piece[16];
        this.currentPieces = whitePieces;
        this.opponentPieces = blackPieces;
    }

    public Piece[][] getPieces() {
        return pieces;
    }
    public void setPieces(Piece[][] pieces) {
        this.pieces = pieces;
    }
    public Button[][] getSquares() {
        return squares;
    }
    public void setSquares(Button[][] squares) {
        this.squares = squares;
    }
    public int getSelectedRow() {
        return selectedRow;
    }
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }
    public int getSelectedCol() {
        return selectedCol;
    }
    public void setSelectedCol(int selectedCol) {
        this.selectedCol = selectedCol;
    }
    public String getTurn() {
        return turn;
    }
    public boolean getInCheck() {
        return inCheck;
    }

    public void setStartPos() {
        // pawns
        for (int i = 0; i < 8; i++) {
            pieces[i][6] = whitePieces[i] = new Pawn("white", i, 6);
            pieces[i][1] = blackPieces[i] = new Pawn("black", i, 1);
        }

        // rooks
        pieces[0][7] = whitePieces[8] = new Rook("white", 0, 7);
        pieces[7][7] = whitePieces[9] = new Rook("white", 7, 7);
        pieces[0][0] = blackPieces[8] = new Rook("black", 0, 0);
        pieces[7][0] = blackPieces[9] = new Rook("black", 7, 0);

        // knights
        pieces[1][7] = whitePieces[10] = new Knight("white", 1, 7);
        pieces[6][7] = whitePieces[11] = new Knight("white", 6, 7);
        pieces[1][0] = blackPieces[10] = new Knight("black", 1, 0);
        pieces[6][0] = blackPieces[11] = new Knight("black", 6, 0);

        // bishops
        pieces[2][7] = whitePieces[12] = new Bishop("white", 2, 7);
        pieces[5][7] = whitePieces[13] = new Bishop("white", 5, 7);
        pieces[2][0] = blackPieces[12] = new Bishop("black", 2, 0);
        pieces[5][0] = blackPieces[13] = new Bishop("black", 5, 0);

        // queens
        pieces[3][7] = whitePieces[14] = new Queen("white", 3, 7);
        pieces[3][0] = blackPieces[14] = new Queen("black", 3, 0);

        // kings
        pieces[4][7] = whitePieces[15] = new King("white", 4, 7);
        pieces[4][0] = blackPieces[15] = new King("black", 4, 0);

        whiteKing = (King) pieces[4][7];
        blackKing = (King) pieces[4][0];
        currentKing = whiteKing;

    }

    // check if move is valid and move the piece
    public void checkMove(int i, int j) {

        if (selectedCol == -1) {
            return; // no piece selected
        }

        Piece selectedPiece = pieces[selectedCol][selectedRow];
        Button selectedSquare = squares[selectedCol][selectedRow];

        // 4 options: valid square, invalid square, same square, or castle (castle is not accounted for in King's isValidMove method)
        if (i == selectedCol && j == selectedRow) {

            // same square: reset selection
            selectedSquare.setStyle("-fx-background-color: transparent;");
            selectedRow = -1;
            selectedCol = -1;

            if (inCheck && selectedPiece instanceof King) {
                // set square back to red
                selectedSquare.setStyle("-fx-background-color: red;");
            }

        }

        // valid square
        else if (selectedPiece.isValidMove(i, j, pieces)) {

            // check if piece is pinned
            if (!checkSafeMove(selectedCol, selectedRow, i, j)) {
                return;
            }

            // update king position if piece is a king and turn off catling
            if (selectedPiece instanceof King) {
                if (currentKing.getCanCastleKSide() || currentKing.getCanCastleQSide()) {
                    currentKing.setCanCastleKSide(false);
                    currentKing.setCanCastleQSide(false);
                }
            }

            // if a rook makes its fist move, the king cannot castle on that side
            else if (selectedPiece instanceof Rook) {
                Rook rook = (Rook) selectedPiece;
                if (rook.getHasMoved() == false) {
                    rook.setHasMoved(true);
                    if (selectedCol == 0) {
                        currentKing.setCanCastleQSide(false);
                    } else if (selectedCol == 7) {
                        currentKing.setCanCastleKSide(false);
                    }
                }
            }

            else if (selectedPiece instanceof Pawn && (turn == "white" && j == 0 || turn == "black" && j == 7)) {
                // promote pawn
                selectedPiece = new Queen(turn, i, j);

            }

            // Move the piece
            // if the move is a capture move, set the captured piece to null (or else it will be included in the oppenent's pieces array)
            if (pieces[i][j] != null) {
                for (int k = 0; k < opponentPieces.length; k++) {
                    if (opponentPieces[k] != null) {
                        if (opponentPieces[k] == pieces[i][j]) {
                            opponentPieces[k] = null;
                            break;
                        }
                    }
                }
            }
            pieces[i][j] = selectedPiece;
            pieces[i][j].setPosition(i,j);
            pieces[selectedCol][selectedRow] = null;

            squares[i][j].setGraphic(new ImageView(pieces[i][j].getIcon()));
            selectedSquare.setGraphic(null);

            selectedSquare.setStyle("-fx-background-color: transparent;");
            selectedRow = -1;
            selectedCol = -1;

            completeTurn();

        } 

        else if (selectedPiece instanceof King && (i == selectedCol + 2 || i == selectedCol - 2) && j == selectedRow) {
            // castle
            boolean castled = false;
            if (i == selectedCol + 2) {
                castled = castle('K');
            } else if (i == selectedCol - 2) {
                castled = castle('Q');
            }
            selectedSquare.setStyle("-fx-background-color: transparent;");
            selectedRow = -1;
            selectedCol = -1;
            if (inCheck) {
                selectedSquare.setStyle("-fx-background-color: red;");
            }

            if (castled) {
                completeTurn();
            }
        }
        
        else { // invalid move
            selectedSquare.setStyle("-fx-background-color: transparent;");
            selectedRow = -1;
            selectedCol = -1;

            if (inCheck && selectedPiece instanceof King) {
                // set square back to red
                selectedSquare.setStyle("-fx-background-color: red;");
            }
        }
    }

    // switches turns
    private void completeTurn() {
        if (turn.equals("white")) {
            turn = "black";
            currentKing = blackKing;
            currentPieces = blackPieces;
            opponentPieces = whitePieces;
        } else {
            turn = "white";
            currentKing = whiteKing;
            currentPieces = whitePieces;
            opponentPieces = blackPieces;
        }


        inCheck = isKingInCheck(opponentPieces, pieces);
        if (inCheck) {
            squares[currentKing.getPositionColumn()][currentKing.getPositionRow()].setStyle("-fx-background-color: red;");
        }
        else {
            squares[whiteKing.getPositionColumn()][whiteKing.getPositionRow()].setStyle("-fx-background-color: transparent;");
            squares[blackKing.getPositionColumn()][blackKing.getPositionRow()].setStyle("-fx-background-color: transparent;");
        }

        if (checkCheckmate()) {
            System.out.println(turn + " is in checkmate!");
        }
    }

    private boolean isKingInCheck(Piece[] p, Piece[][] pieces) {

        for (Piece piece : p) {
            if (piece != null && piece.isValidMove(currentKing.getPositionColumn(), currentKing.getPositionRow(), pieces)) {
                return true; // king is in check
            }
        }
        
        return false; // king is not in check
    }

    // prevents pieces from moving in/into check
    private boolean checkSafeMove(int x1, int y1, int x2, int y2) {
        Piece[][] tempPieces = new Piece[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                tempPieces[x][y] = pieces[x][y];
            }
        }

        tempPieces[x1][y1].setPosition(x2, y2);

        Piece[] tempOpponentPieces = new Piece[16];
        for (int i = 0; i < opponentPieces.length; i++) {
            if (opponentPieces[i] == tempPieces[x2][y2]) { // if piece is captured
                tempOpponentPieces[i] = null;
            }
            else {
                tempOpponentPieces[i] = opponentPieces[i];
            }
        }
        tempPieces[x2][y2] = tempPieces[x1][y1];
        tempPieces[x1][y1] = null;

        boolean kingInCheck = isKingInCheck(tempOpponentPieces, tempPieces);
        tempPieces[x2][y2].setPosition(x1, y1); // reset piece position
        if (kingInCheck) {
            return false; // piece is pinned & cannot move
        } else {
            return true;
        }

    }

    private boolean checkCheckmate() {
        // Check if the king is in checkmate
        if (inCheck) {
    
            // 1. Check if the king can move to a safe square
            for (int xDif = -1; xDif < 2; xDif++) {
                for (int yDif = -1; yDif < 2; yDif++) {
                    if (xDif == 0 && yDif == 0) {
                        continue; // Skip the current position
                    }
    
                    int newX = currentKing.getPositionColumn() + xDif;
                    int newY = currentKing.getPositionRow() + yDif;
                    if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                        if (pieces[newX][newY] == null || !pieces[newX][newY].getColor().equals(turn)) {
                            if (checkSafeMove(currentKing.getPositionColumn(), currentKing.getPositionRow(), newX, newY)) {
                                return false; // King can move to a safe square
                            }
                        }
                    }
                }
            }
    
            // 2. Identify the attacking piece(s)
            Piece attackingPiece = null;
            int attackerCol = -1, attackerRow = -1;
            for (Piece piece : currentPieces) {
                if (piece != null && piece.isValidMove(currentKing.getPositionColumn(), currentKing.getPositionRow(), pieces)) {
                    if (attackingPiece == null) {
                        attackingPiece = piece;
                        //attackerCol = i;
                        //attackerRow = j;
                    }
                    else {
                        // king is in double check & we already determined
                        // that the king cannot move --> checkmate
                        return true;
                    }
                }
            }
    
            // If there is no attacking piece (shouldn't happen), return false
            if (attackingPiece == null) {
                return false;
            }
    
            // 3. Check if the attacking piece can be captured
            for (Piece piece : currentPieces) {
                if (piece != null && piece.isValidMove(attackingPiece.getPositionColumn(), attackingPiece.getPositionRow(), pieces)) {
                    if (checkSafeMove(piece.getPositionColumn(), piece.getPositionRow(), attackingPiece.getPositionColumn(), attackingPiece.getPositionRow())) {
                        return false; // A piece can capture the attacker
                    }
                }
            }
    
            // 4. Check if the attack can be blocked (only rook/bishop/queen can be blocked)
            if (attackingPiece instanceof Rook || attackingPiece instanceof Bishop || attackingPiece instanceof Queen) {
                int dRow = Integer.signum(currentKing.getPositionRow() - attackerRow);
                int dCol = Integer.signum(currentKing.getPositionColumn() - attackerCol);
    
                int blockRow = attackerRow + dRow;
                int blockCol = attackerCol + dCol;
    
                while (blockRow != currentKing.getPositionRow() || blockCol != currentKing.getPositionColumn()) {
                    for (Piece piece : currentPieces) {
                        if (piece != null && piece.isValidMove(blockCol, blockRow, pieces)) {
                            if (checkSafeMove(piece.getPositionColumn(), piece.getPositionRow(), blockCol, blockRow)) {
                                return false; // A piece can block the attack
                            }
                        }
                    }
                    blockRow += dRow;
                    blockCol += dCol;
                }
            }

            return true; // no moves can save king --> checkmate
        }
        return false; // Not in check, so not checkmate
    }

    // return true if successfuly castled
    private boolean castle(char side) {
        int kingCol = currentKing.getPositionColumn();
        int kingRow = currentKing.getPositionRow();
        if (side == 'K') {
            // king cannot be in check or move into check. additionally, rook cannot be under attack in casteled position
            if (isKingInCheck(opponentPieces, pieces) || !checkSafeMove(kingCol,kingRow,kingCol+2,kingRow) || !checkSafeMove(kingCol,kingRow,kingCol+1,kingRow)) {
                return false;
            }
            if (currentKing.getCanCastleKSide() && pieces[kingCol + 1][kingRow] == null && pieces[kingCol + 2][kingRow] == null) {

                pieces[kingCol + 2][kingRow] = pieces[kingCol][kingRow]; // move king
                currentKing.setPosition(kingCol + 2, kingRow); // update king position
                pieces[kingCol + 1][kingRow] = pieces[kingCol + 3][kingRow]; // move rook
                pieces[kingCol + 1][kingRow].setPosition(kingCol + 1, kingRow); // Update rook's position
                pieces[kingCol][kingRow] = null;
                pieces[kingCol + 3][kingRow] = null;

                // update gui
                squares[kingCol + 2][kingRow].setGraphic(new ImageView(pieces[kingCol + 2][kingRow].getIcon()));
                squares[kingCol + 1][kingRow].setGraphic(new ImageView(pieces[kingCol + 1][kingRow].getIcon()));
                squares[kingCol][kingRow].setGraphic(null);
                squares[kingCol + 3][kingRow].setGraphic(null);

                currentKing.setCanCastleKSide(false);
                currentKing.setCanCastleQSide(false);
                return true;
            }

        } else if (side == 'Q') {
            if (isKingInCheck(opponentPieces, pieces) || !checkSafeMove(kingCol, kingRow, kingCol - 2, kingRow) || !checkSafeMove(kingCol, kingRow, kingCol - 1, kingRow)) {
                return false;
            }
            if (currentKing.getCanCastleQSide() && pieces[kingCol - 1][kingRow] == null && pieces[kingCol - 2][kingRow] == null) {
                pieces[kingCol - 2][kingRow] = pieces[kingCol][kingRow]; // move king
                currentKing.setPosition(kingCol - 2, kingRow); // update king position
                pieces[kingCol - 1][kingRow] = pieces[kingCol - 4][kingRow]; // move rook
                pieces[kingCol - 1][kingRow].setPosition(kingCol - 1, kingRow); // Update rook's position
                pieces[kingCol][kingRow] = null;
                pieces[kingCol - 4][kingRow] = null;
                
                // update gui
                squares[kingCol - 2][kingRow].setGraphic(new ImageView(pieces[kingCol - 2][kingRow].getIcon()));
                squares[kingCol - 1][kingRow].setGraphic(new ImageView(pieces[kingCol - 1][kingRow].getIcon()));
                squares[kingCol][kingRow].setGraphic(null);
                squares[kingCol - 4][kingRow].setGraphic(null);
        
                currentKing.setCanCastleKSide(false);
                currentKing.setCanCastleQSide(false);
                return true;
            }
        }
        return false;
    }
}