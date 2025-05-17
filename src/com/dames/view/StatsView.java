package com.dames.view;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class StatsView extends JFrame {
    private JLabel playerNameLabel;
    private JLabel gamesPlayedLabel;
    private JLabel winsLabel;
    private JLabel lossesLabel;
    private JLabel drawsLabel;
    private JLabel winRateLabel;
    private JLabel avgMovesLabel;
    private JLabel capturesLabel;
    private JLabel promotionsLabel;
    private JLabel durationLabel;
    private JPanel rankingsPanel;
    private JButton closeButton;

    public StatsView() {
        setTitle("Statistiques - Jeu de Dames");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Créer le panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Créer le panneau des statistiques
        JPanel statsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialiser les labels
        playerNameLabel = new JLabel("Joueur : ");
        gamesPlayedLabel = new JLabel("Parties jouées : ");
        winsLabel = new JLabel("Victoires : ");
        lossesLabel = new JLabel("Défaites : ");
        drawsLabel = new JLabel("Nuls : ");
        winRateLabel = new JLabel("Taux de victoire : ");
        avgMovesLabel = new JLabel("Moyenne des coups : ");
        capturesLabel = new JLabel("Prises : ");
        promotionsLabel = new JLabel("Promotions : ");
        durationLabel = new JLabel("Durée moyenne : ");

        // Ajouter les labels au panneau
        statsPanel.add(playerNameLabel);
        statsPanel.add(gamesPlayedLabel);
        statsPanel.add(winsLabel);
        statsPanel.add(lossesLabel);
        statsPanel.add(drawsLabel);
        statsPanel.add(winRateLabel);
        statsPanel.add(avgMovesLabel);
        statsPanel.add(capturesLabel);
        statsPanel.add(promotionsLabel);
        statsPanel.add(durationLabel);

        // Créer le panneau des classements
        JPanel rankingsContainer = new JPanel(new BorderLayout());
        rankingsContainer.setBorder(BorderFactory.createTitledBorder("Classement des joueurs"));
        rankingsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(rankingsPanel);
        rankingsContainer.add(scrollPane, BorderLayout.CENTER);

        // Créer le bouton de fermeture
        closeButton = new JButton("Fermer");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);

        // Ajouter les composants au panneau principal
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(rankingsContainer, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);
    }

    public void setPlayerName(String name) {
        playerNameLabel.setText("Joueur : " + name);
    }

    public void setGamesPlayed(int count) {
        gamesPlayedLabel.setText("Parties jouées : " + count);
    }

    public void setWins(int count) {
        winsLabel.setText("Victoires : " + count);
    }

    public void setLosses(int count) {
        lossesLabel.setText("Défaites : " + count);
    }

    public void setDraws(int count) {
        drawsLabel.setText("Nuls : " + count);
    }

    public void setWinRate(double rate) {
        winRateLabel.setText(String.format("Taux de victoire : %.1f%%", rate));
    }

    public void setAvgMoves(double moves) {
        avgMovesLabel.setText(String.format("Moyenne des coups : %.1f", moves));
    }

    public void setCaptures(int count) {
        capturesLabel.setText("Prises : " + count);
    }

    public void setPromotions(int count) {
        promotionsLabel.setText("Promotions : " + count);
    }

    public void setDuration(String duration) {
        durationLabel.setText("Durée moyenne : " + duration);
    }

    public void updateRankings(List<Map<String, Object>> rankings) {
        rankingsPanel.removeAll();
        for (Map<String, Object> player : rankings) {
            JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            playerPanel.add(new JLabel(String.format("%s - %d victoires", 
                player.get("username"), 
                player.get("wins"))));
            rankingsPanel.add(playerPanel);
        }
        rankingsPanel.revalidate();
        rankingsPanel.repaint();
    }

    public void addCloseButtonListener(java.awt.event.ActionListener listener) {
        closeButton.addActionListener(listener);
    }
}