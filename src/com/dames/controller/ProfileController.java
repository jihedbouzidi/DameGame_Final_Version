package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Database;
import model.Player;
import view.ProfileView;

public class ProfileController {
    private final ProfileView view;
    public Database db;
    private final Player player;

    public ProfileController(ProfileView view, Database db, Player player) {
        this.view = view;
        this.db = db;
        this.player = player;
        
        view.addSaveListener(new SaveListener());
        view.addCancelListener(new CancelListener());
    }

    class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String currentPassword = new String(view.getCurrentPassword());
            String newPassword = new String(view.getNewPassword());
            String confirmPassword = new String(view.getConfirmPassword());
            String avatar = view.getAvatar();
            
            // Vérifier le mot de passe actuel
            if (!currentPassword.equals(player.getPassword())) {
                view.showMessage("Mot de passe actuel incorrect");
                return;
            }
            
            // Vérifier que les nouveaux mots de passe correspondent
            if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
                view.showMessage("Les nouveaux mots de passe ne correspondent pas");
                return;
            }
            
            // Si le nouveau mot de passe est vide, garder l'ancien
            String finalPassword = newPassword.isEmpty() ? player.getPassword() : newPassword;
            
            // Mettre à jour le profil
            boolean success = db.updatePlayerProfile(
                player.getUsername(),
                finalPassword,
                avatar.isEmpty() ? player.getAvatar() : avatar
            );
            
            if (success) {
                player.setPassword(finalPassword);
                if (!avatar.isEmpty()) {
                    player.setAvatar(avatar);
                }
                view.showMessage("Profil mis à jour avec succès");
                view.dispose();
            } else {
                view.showMessage("Erreur lors de la mise à jour du profil");
            }
        }
    }

    class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }
}