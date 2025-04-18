package edu.guilford;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Game {
    private Piece[][] pieces; // 2D array to hold the pieces on the board
    private Button[][] squares;
    private int selectedRow;
    private int selectedCol;

    private int whiteKingRow, whiteKingCol;
    private int blackKingRow, blackKingCol;
    private int turnKingRow, turnKingCol; // track the king's position of the current turn

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
        this.whiteKingRow = 7;
        this.whiteKingCol = 4;
        this.blackKingRow = 0;
        this.blackKingCol = 4;
        this.turn = "white";
        this.inCheck = false;
        this.turnKingRow = whiteKingRow;
        this.turnKingCol = whiteKingCol;
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
                updateKingPosition(turnKingCol, turnKingRow, i, j);
                King king = (King) selectedPiece;
                if (king.getCanCastleKSide() || king.getCanCastleQSide()) {
                    king.setCanCastleKSide(false);
                    king.setCanCastleQSide(false);
                }
            }

            // if a rook makes its fist move, the king cannot castle on that side
            else if (selectedPiece instanceof Rook) {
                Rook rook = (Rook) selectedPiece;
                if (rook.getHasMoved() == false) {
                    rook.setHasMoved(true);
                    King king = (King) pieces[turnKingCol][turnKingRow];
                    if (selectedCol == 0) {
                        king.setCanCastleQSide(false);
                    } else if (selectedCol == 7) {
                        king.setCanCastleKSide(false);
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
            turnKingCol = blackKingCol;
            turnKingRow = blackKingRow;
            currentPieces = blackPieces;
            opponentPieces = whitePieces;
        } else {
            turn = "white";
            turnKingCol = whiteKingCol;
            turnKingRow = whiteKingRow;
            currentPieces = whitePieces;
            opponentPieces = blackPieces;
        }


        inCheck = isKingInCheck(opponentPieces, pieces);
        if (inCheck) {
            squares[turnKingCol][turnKingRow].setStyle("-fx-background-color: red;");
        }
        else {
            squares[whiteKingCol][whiteKingRow].setStyle("-fx-background-color: transparent;");
            squares[blackKingCol][blackKingRow].setStyle("-fx-background-color: transparent;");
        }

        if (checkCheckmate()) {
            System.out.println(turn + " is in checkmate!");
        }
    }

    private boolean isKingInCheck(Piece[] p, Piece[][] pieces) {

        for (Piece piece : p) {
            if (piece != null && piece.isValidMove(turnKingCol, turnKingRow, pieces)) {
                return true; // king is in check
            }
        }
        
    /*
        // iterate through all opponent pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null && !pieces[i][j].getColor().equals(color)) {
                    // check if the opponent piece can attack the king
                    if (pieces[i][j].isValidMove(turnKingCol, turnKingRow, pieces)) {
                        return true; // king is in check
                    }
                }
            }
        }
    */
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

        if (tempPieces[x1][y1] instanceof King) {
            //updateKingPosition(x1, y1, x2, y2);
            turnKingCol = x2;
            turnKingRow = y2;
        }

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
        if (tempPieces[x2][y2] instanceof King) {
            //updateKingPosition(x1, y1, x1, y1);
            turnKingCol = x1;
            turnKingRow = y1;
        }
        if (kingInCheck) {
            return false; // piece is pinned & cannot move
        } else {
            return true;
        }

    }

    private void updateKingPosition(int kingCol, int kingRow, int i, int j) {
        if (pieces[kingCol][kingRow].getColor().equals("white")) {
            whiteKingCol = i;
            whiteKingRow = j;
        } else {
            blackKingCol = i;
            blackKingRow = j;
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
    
                    int newX = turnKingCol + xDif;
                    int newY = turnKingRow + yDif;
                    if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                        if (pieces[newX][newY] == null || !pieces[newX][newY].getColor().equals(turn)) {
                            if (checkSafeMove(turnKingCol, turnKingRow, newX, newY)) {
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
                if (piece != null && piece.isValidMove(turnKingCol, turnKingRow, pieces)) {
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
                int dRow = Integer.signum(turnKingRow - attackerRow);
                int dCol = Integer.signum(turnKingCol - attackerCol);
    
                int blockRow = attackerRow + dRow;
                int blockCol = attackerCol + dCol;
    
                while (blockRow != turnKingRow || blockCol != turnKingCol) {
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
        King king = (King) pieces[turnKingCol][turnKingRow];
        if (side == 'K') {
            // king cannot be in check or move into check. additionally, rook cannot be under attack in casteled position
            if (isKingInCheck(opponentPieces, pieces) || !checkSafeMove(turnKingCol,turnKingRow,turnKingCol+2,turnKingRow) || !checkSafeMove(turnKingCol,turnKingRow,turnKingCol+1,turnKingRow)) {
                return false;
            }
            if (king.getCanCastleKSide() && pieces[turnKingCol + 1][turnKingRow] == null && pieces[turnKingCol + 2][turnKingRow] == null) {

                pieces[turnKingCol + 2][turnKingRow] = pieces[turnKingCol][turnKingRow]; // move king
                pieces[turnKingCol + 2][turnKingRow].setPosition(turnKingCol + 2, turnKingRow);
                pieces[turnKingCol + 1][turnKingRow] = pieces[turnKingCol + 3][turnKingRow]; // move rook
                pieces[turnKingCol + 1][turnKingRow].setPosition(turnKingCol + 1, turnKingRow); // Update rook's position
                pieces[turnKingCol][turnKingRow] = null;
                pieces[turnKingCol + 3][turnKingRow] = null;

                squares[turnKingCol + 2][turnKingRow].setGraphic(new ImageView(pieces[turnKingCol + 2][turnKingRow].getIcon()));
                squares[turnKingCol + 1][turnKingRow].setGraphic(new ImageView(pieces[turnKingCol + 1][turnKingRow].getIcon()));
                squares[turnKingCol][turnKingRow].setGraphic(null);
                squares[turnKingCol + 3][turnKingRow].setGraphic(null);

                king.setCanCastleKSide(false);
                king.setCanCastleQSide(false);
                return true;
            }

        } else if (side == 'Q') {
            if (isKingInCheck(opponentPieces, pieces) || !checkSafeMove(turnKingCol,turnKingRow,turnKingCol-2,turnKingRow) || !checkSafeMove(turnKingCol,turnKingRow,turnKingCol-1,turnKingRow)) {
                return false;
            }
            if (king.getCanCastleQSide() && pieces[turnKingCol - 1][turnKingRow] == null && pieces[turnKingCol - 2][turnKingRow] == null) {
                pieces[turnKingCol - 2][turnKingRow] = pieces[turnKingCol][turnKingRow];
                pieces[turnKingCol - 1][turnKingRow] = pieces[turnKingCol - 4][turnKingRow];
                pieces[turnKingCol][turnKingRow] = null;
                pieces[turnKingCol - 4][turnKingRow] = null;

                squares[turnKingCol - 2][turnKingRow].setGraphic(new ImageView(pieces[turnKingCol - 2][turnKingRow].getIcon()));
                squares[turnKingCol - 1][turnKingRow].setGraphic(new ImageView(pieces[turnKingCol - 1][turnKingRow].getIcon()));
                squares[turnKingCol][turnKingRow].setGraphic(null);
                squares[turnKingCol - 4][turnKingRow].setGraphic(null);

                king.setCanCastleKSide(false);
                king.setCanCastleQSide(false);
                return true;
            }
        }
        return false;
    }
}