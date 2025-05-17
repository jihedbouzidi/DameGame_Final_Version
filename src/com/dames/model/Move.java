package com.dames.model;

public class Move {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private boolean isCapture;
    private int capturedRow;
    private int capturedCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.isCapture = false;
    }

    public Move(int fromRow, int fromCol, int toRow, int toCol, int capturedRow, int capturedCol) {
        this(fromRow, fromCol, toRow, toCol);
        this.isCapture = true;
        this.capturedRow = capturedRow;
        this.capturedCol = capturedCol;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public int getCapturedRow() {
        return capturedRow;
    }

    public int getCapturedCol() {
        return capturedCol;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(fromRow).append(",").append(fromCol).append(") -> ");
        sb.append("(").append(toRow).append(",").append(toCol).append(")");
        if (isCapture) {
            sb.append(" [Capture: (").append(capturedRow).append(",").append(capturedCol).append(")]");
        }
        return sb.toString();
    }
}