package com.dames.main;

import com.dames.controller.LoginController;
import com.dames.model.Database;
import com.dames.view.LoginView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Définir le look and feel du système
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Créer la base de données
        Database database = new Database();

        // Créer la vue de connexion
        LoginView loginView = new LoginView();

        // Créer le contrôleur de connexion
        LoginController loginController = new LoginController(loginView, database);

        // Afficher la vue de connexion
        loginView.setVisible(true);
    }
}