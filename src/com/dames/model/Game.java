package com.dames.model;

import com.dames.view.GameView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class Game {
    public static final int BOARD_SIZE = 10;
    private Board board;
    private String currentPlayer;
    private String playerId;
    private String gameId;
    private int moveCount;
    private int captureCount;
    private int promotionCount;
    private long startTime;
    private String winner;
    private final String difficulty;
    private GameView view;
    private boolean isHumanTurn;

    public Game(String playerColor, String difficulty) {
        this.board = new Board();
        this.currentPlayer = "white";
        this.playerId = playerId;
        this.gameId = String.valueOf(System.currentTimeMillis());
        this.moveCount = 0;
        this.captureCount = 0;
        this.promotionCount = 0;
        this.startTime = System.currentTimeMillis();
        this.winner = null;
        this.difficulty = difficulty;
        
        this.isHumanTurn = playerColor.equals("white");
        
        board.initializeBoard(playerColor);
    }

    public void setView(GameView view) {
        this.view = view;
        updateStatus();
        if (!isHumanTurn) {
            computerTurn();
        }
    }

    public boolean isValidPiece(int row, int col) {
        Piece piece = board.getPiece(row, col);
        return piece != null && piece.getColor().equals(currentPlayer);
    }

    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }
        return piece.isValidMove(board, fromRow, fromCol, toRow, toCol);
    }

    public List<Move> getValidMoves(int row, int col) {
        List<Move> moves = new ArrayList<>();
        Piece piece = board.getPiece(row, col);
        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return moves;
        }

        // Vérifier les captures obligatoires
        if (board.hasMandatoryCaptures(currentPlayer)) {
            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    if (piece.isValidMove(board, row, col, r, c)) {
                        moves.add(new Move(row, col, r, c));
                    }
                }
            }
        } else {
            // Vérifier les mouvements simples
            for (int r = 0; r < BOARD_SIZE; r++) {
                for (int c = 0; c < BOARD_SIZE; c++) {
                    if (piece.isValidMove(board, row, col, r, c)) {
                        moves.add(new Move(row, col, r, c));
                    }
                }
            }
        }
        return moves;
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        Piece piece = board.getPiece(fromRow, fromCol);
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        // Vérifier si c'est une capture
        if (Math.abs(rowDiff) == 2) {
            int capturedRow = fromRow + rowDiff / 2;
            int capturedCol = fromCol + colDiff / 2;
            board.removePiece(capturedRow, capturedCol);
            captureCount++;
        }

        // Déplacer la pièce
        board.movePiece(fromRow, fromCol, toRow, toCol);

        // Vérifier la promotion
        if (piece instanceof Pawn) {
            if ((piece.getColor().equals("white") && toRow == 0) ||
                (piece.getColor().equals("black") && toRow == BOARD_SIZE - 1)) {
                board.promotePiece(toRow, toCol);
                promotionCount++;
            }
        }

        moveCount++;
        currentPlayer = currentPlayer.equals("white") ? "black" : "white";
        isHumanTurn = currentPlayer.equals(playerId);

        // Vérifier si la partie est terminée
        if (isGameOver()) {
            winner = currentPlayer.equals("white") ? "black" : "white";
        }

        if (!isHumanTurn) {
            computerTurn();
        }

        return true;
    }

    public void computerTurn() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                
                List<Move> captureSequence = findBestCaptureSequence();
                if (!captureSequence.isEmpty()) {
                    executeCaptureSequence(captureSequence);
                } else {
                    // Pas de captures possibles, faire un mouvement normal
                    switch (difficulty) {
                        case "easy":
                            makeRandomMove();
                            break;
                        case "medium":
                            makeMediumMove();
                            break;
                        case "hard":
                            makeHardMove();
                            break;
                        default:
                            makeRandomMove();
                    }
                }
                
                SwingUtilities.invokeLater(() -> {
                    view.drawBoard(board);
                    updateStatus();
                    checkGameOver();
                    if (isGameOver()) {
                        endGame();
                    }
                });
            } catch (InterruptedException e) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
            }
        }).start();
    }

    private List<Move> findBestCaptureSequence() {
        List<Move> bestSequence = new ArrayList<>();
        String[][] originalState = board.getBoardState();
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    List<Move> currentSequence = new ArrayList<>();
                    findCaptureSequenceRecursive(piece, row, col, currentSequence, bestSequence, originalState);
                }
            }
        }
        
        return bestSequence;
    }

    private void findCaptureSequenceRecursive(Piece piece, int row, int col, 
                                           List<Move> currentSequence, 
                                           List<Move> bestSequence,
                                           String[][] originalState) {
        boolean foundCapture = false;
        
        for (int[] dir : piece.getCaptureDirections()) {
            if (piece instanceof Queen) {
                for (int distance = 1; distance < BOARD_SIZE; distance++) {
                    int newRow = row + dir[0] * distance;
                    int newCol = col + dir[1] * distance;
                    
                    if (newRow < 0 || newRow >= BOARD_SIZE || newCol < 0 || newCol >= BOARD_SIZE) {
                        break;
                    }
                    
                    if (piece.canCapture(newRow, newCol, board)) {
                        Move move = new Move(row, col, newRow, newCol);
                        currentSequence.add(move);
                        foundCapture = true;
                        
                        // Sauvegarder l'état actuel
                        String[][] savedState = board.getBoardState();
                        
                        // Effectuer le mouvement
                        if (piece instanceof Queen) {
                            int[] capturedPos = findCapturedPieceForQueen(row, col, newRow, newCol);
                            board.movePiece(row, col, newRow, newCol);
                            board.capturePiece(capturedPos[0], capturedPos[1]);
                        } else {
                            int middleRow = (row + newRow) / 2;
                            int middleCol = (col + newCol) / 2;
                            board.movePiece(row, col, newRow, newCol);
                            board.capturePiece(middleRow, middleCol);
                        }
                        
                        // Vérifier les captures supplémentaires
                        Piece movedPiece = board.getPiece(newRow, newCol);
                        if (movedPiece != null) {
                            findCaptureSequenceRecursive(movedPiece, newRow, newCol, 
                                                      currentSequence, bestSequence, originalState);
                        }
                        
                        // Restaurer l'état original
                        board.setBoardState(savedState);
                        currentSequence.remove(currentSequence.size() - 1);
                    }
                }
            } else {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE) {
                    if (piece.canCapture(newRow, newCol, board)) {
                        Move move = new Move(row, col, newRow, newCol);
                        currentSequence.add(move);
                        foundCapture = true;
                        
                        // Sauvegarder l'état actuel
                        String[][] savedState = board.getBoardState();
                        
                        // Effectuer le mouvement
                        int middleRow = (row + newRow) / 2;
                        int middleCol = (col + newCol) / 2;
                        board.movePiece(row, col, newRow, newCol);
                        board.capturePiece(middleRow, middleCol);
                        
                        // Vérifier les captures supplémentaires
                        Piece movedPiece = board.getPiece(newRow, newCol);
                        if (movedPiece != null) {
                            findCaptureSequenceRecursive(movedPiece, newRow, newCol, 
                                                      currentSequence, bestSequence, originalState);
                        }
                        
                        // Restaurer l'état original
                        board.setBoardState(savedState);
                        currentSequence.remove(currentSequence.size() - 1);
                    }
                }
            }
        }
        
        if (!foundCapture && currentSequence.size() > bestSequence.size()) {
            bestSequence.clear();
            bestSequence.addAll(currentSequence);
        }
    }

    private void executeCaptureSequence(List<Move> sequence) {
        for (Move move : sequence) {
            try {
                Thread.sleep(1000); // Pause entre chaque capture
                
                Piece piece = board.getPiece(move.getFromRow(), move.getFromCol());
                if (piece instanceof Queen) {
                    int[] capturedPos = findCapturedPieceForQueen(move.getFromRow(), move.getFromCol(), 
                                                                move.getToRow(), move.getToCol());
                    if (capturedPos != null) {
                        board.movePiece(move.getFromRow(), move.getFromCol(), 
                                      move.getToRow(), move.getToCol());
                        board.capturePiece(capturedPos[0], capturedPos[1]);
                    }
                } else {
                    int middleRow = (move.getFromRow() + move.getToRow()) / 2;
                    int middleCol = (move.getFromCol() + move.getToCol()) / 2;
                    board.movePiece(move.getFromRow(), move.getFromCol(), 
                                  move.getToRow(), move.getToCol());
                    board.capturePiece(middleRow, middleCol);
                }
                
                SwingUtilities.invokeLater(() -> {
                    view.drawBoard(board);
                    updateStatus();
                });
            } catch (InterruptedException e) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        checkGameOver();
    }

    private int[] findCapturedPieceForQueen(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;
        if (Math.abs(rowDiff) != Math.abs(colDiff)) {
            return null;
        }

        int rowStep = rowDiff > 0 ? 1 : -1;
        int colStep = colDiff > 0 ? 1 : -1;
        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;

        while (currentRow != toRow && currentCol != toCol) {
            Piece piece = board.getPiece(currentRow, currentCol);
            if (piece != null && !piece.getColor().equals(currentPlayer)) {
                return new int[]{currentRow, currentCol};
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return null;
    }

    private void checkGameOver() {
        boolean whiteHasPieces = false;
        boolean blackHasPieces = false;
        boolean whiteCanMove = false;
        boolean blackCanMove = false;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    if (piece.getColor().equals("white")) {
                        whiteHasPieces = true;
                        if (piece.hasAvailableMoves(board)) {
                            whiteCanMove = true;
                        }
                    } else {
                        blackHasPieces = true;
                        if (piece.hasAvailableMoves(board)) {
                            blackCanMove = true;
                        }
                    }
                }
            }
        }

        // Vérifier si un joueur n'a plus de pions
        if (!whiteHasPieces) {
            winner = "black";
        } else if (!blackHasPieces) {
            winner = "white";
        }
        
        // Vérifier si le joueur actuel peut jouer
        if (currentPlayer.equals("white") && !whiteCanMove) {
            winner = "black";
        } else if (currentPlayer.equals("black") && !blackCanMove) {
            winner = "white";
        }
    }

    public void endGame() {
        SwingUtilities.invokeLater(() -> {
            if (winner.equals(playerId)) {
                view.showMessage("Félicitations! Vous avez gagné!");
            } else {
                view.showMessage("L'ordinateur a gagné! Vous avez perdu.");
            }
        });
    }

    private void makeRandomMove() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor().equals(currentPlayer)) {
                    int[][] directions = piece.getMoveDirections();
                    for (int[] dir : directions) {
                        int newRow = row + dir[0];
                        int newCol = col + dir[1];
                        if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE) {
                            if (makeComputerMove(row, col, newRow, newCol)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
        checkGameOver();
    }

    private boolean makeComputerMove(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || !piece.getColor().equals(currentPlayer)) {
            return false;
        }

        if (!board.hasMandatoryCaptures(currentPlayer) && piece.isValidMove(toRow, toCol, board)) {
            board.movePiece(fromRow, fromCol, toRow, toCol);
            checkGameOver();
            return true;
        }
        return false;
    }

    private void makeMediumMove() {
        // Try to promote a pawn
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof Pawn && piece.getColor().equals(currentPlayer)) {
                    if (currentPlayer.equals("white") && row == 1) {
                        if (makeComputerMove(row, col, row-1, col-1) || 
                            makeComputerMove(row, col, row-1, col+1)) {
                            return;
                        }
                    } else if (currentPlayer.equals("black") && row == BOARD_SIZE-2) {
                        if (makeComputerMove(row, col, row+1, col-1) || 
                            makeComputerMove(row, col, row+1, col+1)) {
                            return;
                        }
                    }
                }
            }
        }
        makeRandomMove();
    }

    private void makeHardMove() {
        makeMediumMove();
    }

    public boolean isGameOver() { return winner != null; }
    public String getWinner() { return winner; }
    public String getCurrentPlayer() { return currentPlayer; }
    public String getPlayerId() { return playerId; }
    public String getGameId() { return gameId; }
    public int getMoveCount() { return moveCount; }
    public int getCaptureCount() { return captureCount; }
    public int getPromotionCount() { return promotionCount; }
    public long getGameDuration() { return System.currentTimeMillis() - startTime; }
    public String getBoardState() { return board.toString(); }
    public Board getBoard() { return board; }
    public String getHumanPlayerColor() { return currentPlayer; }
    public boolean isHumanTurn() { return isHumanTurn; }

    private void updateStatus() {
        String status = "Tour: " + (currentPlayer.equals("white") ? "Blancs" : "Noirs");
        if (isHumanTurn) {
            status += " (Votre tour)";
        } else {
            status += " (Tour de l'ordinateur)";
        }
        view.setStatus(status);
    }
}