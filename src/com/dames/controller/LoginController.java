package com.dames.controller;

import com.dames.model.Database;
import com.dames.model.Player;
import com.dames.view.LoginView;
import com.dames.view.MainMenuView;
import com.dames.view.RegisterView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private final LoginView view;
    private final Database db;

    public LoginController(LoginView view, Database db) {
        this.view = view;
        this.db = db;
        
        // Ajouter les listeners
        view.getLoginButton().addActionListener(new LoginListener());
        view.getRegisterButton().addActionListener(new RegisterListener());
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsernameField().getText();
            String password = new String(view.getPasswordField().getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Player player = db.authenticatePlayer(username, password);
            if (player != null) {
                view.dispose();
                MainMenuView mainMenuView = new MainMenuView();
                new MainMenuController(mainMenuView, db, player);
                mainMenuView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view, "Nom d'utilisateur ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
            RegisterView registerView = new RegisterView();
            new RegisterController(registerView, db);
            registerView.setVisible(true);
        }
    }
}