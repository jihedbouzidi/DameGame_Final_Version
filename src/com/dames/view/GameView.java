package com.dames.view;

import java.awt.*;
import java.awt.event.MouseListener;
import javax.swing.*;

public class GameView extends JFrame {
    private JPanel boardPanel;
    private JLabel statusLabel;
    private JButton saveButton;
    private JButton quitButton;
    private JButton[][] squares;
    private static final int BOARD_SIZE = 10;
    private static final int SQUARE_SIZE = 60;

    public GameView() {
        setTitle("Jeu de Dames");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(BOARD_SIZE * SQUARE_SIZE + 40, BOARD_SIZE * SQUARE_SIZE + 100);
        setLocationRelativeTo(null);
        setResizable(false);

        // Créer le panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Créer le plateau
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        squares = new JButton[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();

        // Créer le panneau de statut
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel("Au tour des blancs");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(statusLabel);

        // Créer le panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Sauvegarder");
        quitButton = new JButton("Quitter");
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        // Ajouter les composants au panneau principal
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.BLACK);
                square.setBorderPainted(false);
                square.setFocusPainted(false);
                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
    }

    public void setSquarePiece(int row, int col, String piece) {
        JButton square = squares[row][col];
        square.setIcon(new ImageIcon(getClass().getResource("/images/" + piece + ".png")));
    }

    public void clearSquare(int row, int col) {
        squares[row][col].setIcon(null);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void addSquareListener(MouseListener listener) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].addMouseListener(listener);
            }
        }
    }

    public void addSaveButtonListener(java.awt.event.ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addQuitButtonListener(java.awt.event.ActionListener listener) {
        quitButton.addActionListener(listener);
    }

    public JButton getSquare(int row, int col) {
        return squares[row][col];
    }

    public void highlightSquare(int row, int col) {
        squares[row][col].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    public void clearHighlights() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].setBorder(null);
            }
        }
    }
}