package com.dames.model;

public class Queen extends Piece {
    private int row;
    private int col;

    public Queen(String color) {
        super(color);
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Vérifier si la case de destination est vide
        if (board.getPiece(toRow, toCol) != null) {
            return false;
        }

        // Calculer la direction du mouvement
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        // Vérifier si le mouvement est en diagonale
        if (Math.abs(rowDiff) != Math.abs(colDiff)) {
            return false;
        }

        // Vérifier si c'est un mouvement simple
        if (Math.abs(rowDiff) == 1) {
            return true;
        }

        // Vérifier si c'est une capture
        if (Math.abs(rowDiff) == 2) {
            int capturedRow = fromRow + rowDiff / 2;
            int capturedCol = fromCol + colDiff / 2;
            Piece capturedPiece = board.getPiece(capturedRow, capturedCol);
            return capturedPiece != null && !capturedPiece.getColor().equals(color);
        }

        // Vérifier les mouvements multiples
        int rowStep = rowDiff > 0 ? 1 : -1;
        int colStep = colDiff > 0 ? 1 : -1;
        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;
        boolean foundPiece = false;

        while (currentRow != toRow && currentCol != toCol) {
            Piece piece = board.getPiece(currentRow, currentCol);
            if (piece != null) {
                if (foundPiece) {
                    return false; // Plus d'une pièce sur le chemin
                }
                if (piece.getColor().equals(color)) {
                    return false; // Pièce de même couleur
                }
                foundPiece = true;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }

    @Override
    public boolean hasAvailableCaptures(Board board) {
        int[][] directions = {
            {-2, -2}, {-2, 2}, {2, -2}, {2, 2}
        };

        for (int[] dir : directions) {
            int toRow = row + dir[0];
            int toCol = col + dir[1];
            if (toRow >= 0 && toRow < Board.SIZE && toCol >= 0 && toCol < Board.SIZE) {
                if (isValidMove(board, row, col, toRow, toCol)) {
                    return true;
                }
            }
        }
        return false;
    }
}