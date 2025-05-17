package com.dames.view;

import java.awt.*;
import javax.swing.*;

public class MainMenuView extends JFrame {
    private JButton newGameButton;
    private JButton loadGameButton;
    private JButton statsButton;
    private JButton quitButton;
    private JLabel welcomeLabel;

    public MainMenuView(String username) {
        setTitle("Menu Principal - Jeu de Dames");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        // Créer le panneau principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Message de bienvenue
        welcomeLabel = new JLabel("Bienvenue " + username + " !");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(welcomeLabel, gbc);

        // Boutons
        newGameButton = new JButton("Nouvelle partie");
        loadGameButton = new JButton("Charger une partie");
        statsButton = new JButton("Statistiques");
        quitButton = new JButton("Quitter");

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(newGameButton, gbc);

        gbc.gridy = 2;
        mainPanel.add(loadGameButton, gbc);

        gbc.gridy = 3;
        mainPanel.add(statsButton, gbc);

        gbc.gridy = 4;
        mainPanel.add(quitButton, gbc);

        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);
    }

    // Getters
    public JButton getNewGameButton() { return newGameButton; }
    public JButton getLoadGameButton() { return loadGameButton; }
    public JButton getStatsButton() { return statsButton; }
    public JButton getQuitButton() { return quitButton; }
    public JLabel getWelcomeLabel() { return welcomeLabel; }
}