package com.dames.controller;

import com.dames.model.*;
import com.dames.view.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;

public class GameController {
    private Game game;
    private GameView view;
    private Database database;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public GameController(Game game, GameView view, Database database) {
        this.game = game;
        this.view = view;
        this.database = database;
        setupListeners();
        updateView();
    }

    private void setupListeners() {
        view.addSquareListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton square = (JButton) e.getSource();
                for (int row = 0; row < Game.BOARD_SIZE; row++) {
                    for (int col = 0; col < Game.BOARD_SIZE; col++) {
                        if (view.getSquare(row, col) == square) {
                            handleSquareClick(row, col);
                            return;
                        }
                    }
                }
            }
        });

        view.addSaveButtonListener(e -> saveGame());
        view.addQuitButtonListener(e -> quitGame());
    }

    private void handleSquareClick(int row, int col) {
        if (selectedRow == -1) {
            // Première sélection
            if (game.isValidPiece(row, col)) {
                selectedRow = row;
                selectedCol = col;
                view.highlightSquare(row, col);
                showPossibleMoves(row, col);
            }
        } else {
            // Deuxième sélection
            if (game.isValidMove(selectedRow, selectedCol, row, col)) {
                game.makeMove(selectedRow, selectedCol, row, col);
                view.clearHighlights();
                selectedRow = -1;
                selectedCol = -1;
                updateView();
                checkGameEnd();
            } else if (row == selectedRow && col == selectedCol) {
                // Désélection
                view.clearHighlights();
                selectedRow = -1;
                selectedCol = -1;
            }
        }
    }

    private void showPossibleMoves(int row, int col) {
        List<int[]> moves = game.getValidMoves(row, col);
        for (int[] move : moves) {
            view.highlightSquare(move[0], move[1]);
        }
    }

    private void updateView() {
        // Mettre à jour le plateau
        for (int row = 0; row < Game.BOARD_SIZE; row++) {
            for (int col = 0; col < Game.BOARD_SIZE; col++) {
                Piece piece = game.getPiece(row, col);
                if (piece != null) {
                    String pieceType = piece.getColor() + (piece instanceof Queen ? "Queen" : "Pawn");
                    view.setSquarePiece(row, col, pieceType);
                } else {
                    view.clearSquare(row, col);
                }
            }
        }

        // Mettre à jour le statut
        String status = "Au tour des " + (game.getCurrentPlayer().equals("white") ? "blancs" : "noirs");
        view.setStatus(status);
    }

    private void checkGameEnd() {
        if (game.isGameOver()) {
            String winner = game.getWinner();
            String message = winner == null ? "Match nul !" : 
                           "Les " + (winner.equals("white") ? "blancs" : "noirs") + " ont gagné !";
            JOptionPane.showMessageDialog(view, message, "Fin de partie", JOptionPane.INFORMATION_MESSAGE);
            saveGameStats();
        }
    }

    private void saveGame() {
        GameState gameState = new GameState(
            game.getGameId(),
            game.getCurrentPlayerId(),
            game.getCurrentPlayer(),
            game.getBoardState(),
            game.getCurrentPlayer(),
            System.currentTimeMillis()
        );
        database.saveGame(gameState);
        JOptionPane.showMessageDialog(view, "Partie sauvegardée avec succès !");
    }

    private void saveGameStats() {
        // Sauvegarder les statistiques de la partie
        database.updatePlayerStats(
            game.getCurrentPlayerId(),
            game.getWinner() != null,
            game.getMoveCount(),
            game.getCaptureCount(),
            game.getPromotionCount(),
            game.getGameDuration()
        );
    }

    private void quitGame() {
        int choice = JOptionPane.showConfirmDialog(
            view,
            "Voulez-vous sauvegarder la partie avant de quitter ?",
            "Quitter",
            JOptionPane.YES_NO_CANCEL_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            saveGame();
            view.dispose();
        } else if (choice == JOptionPane.NO_OPTION) {
            view.dispose();
        }
    }
}