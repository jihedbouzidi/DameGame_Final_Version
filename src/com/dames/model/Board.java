package com.dames.model;

public class Board {
    private Piece[][] pieces;
    public static final int SIZE = 10;

    public Board() {
        pieces = new Piece[SIZE][SIZE];
    }

    public void initializeBoard(String humanColor) {
        // Initialiser les pions noirs
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    pieces[row][col] = new Pawn("black");
                }
            }
        }

        // Initialiser les pions blancs
        for (int row = 6; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    pieces[row][col] = new Pawn("white");
                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            return pieces[row][col];
        }
        return null;
    }

    public void setPiece(int row, int col, Piece piece) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            pieces[row][col] = piece;
        }
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        pieces[toRow][toCol] = pieces[fromRow][fromCol];
        pieces[fromRow][fromCol] = null;
    }

    public void removePiece(int row, int col) {
        pieces[row][col] = null;
    }

    public void promotePiece(int row, int col) {
        Piece piece = pieces[row][col];
        if (piece instanceof Pawn) {
            pieces[row][col] = new Queen(piece.getColor());
        }
    }

    public boolean hasMandatoryCaptures(String color) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = pieces[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    if (piece.hasAvailableCaptures(this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = pieces[row][col];
                if (piece == null) {
                    sb.append(".");
                } else if (piece instanceof Queen) {
                    sb.append(piece.getColor().equals("white") ? "Q" : "q");
                } else {
                    sb.append(piece.getColor().equals("white") ? "P" : "p");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}