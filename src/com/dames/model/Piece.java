package com.dames.model;

public abstract class Piece {
    protected String color;

    public Piece(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public abstract boolean isValidMove(Board board, int fromRow, int fromCol, int toRow, int toCol);
    public abstract boolean hasAvailableCaptures(Board board);
}