package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Database;
import view.RegisterView;

public class RegisterController {
    private final RegisterView view;
    private final Database db;

    public RegisterController(RegisterView view, Database db) {
        this.view = view;
        this.db = db;
        
        view.addRegisterListener(new RegisterListener());
        view.addCancelListener(new CancelListener());
    }

    class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.getUsername();
            String password = view.getPassword();
            String firstName = view.getFirstName();
            String lastName = view.getLastName();
            
            if (username.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                view.showMessage("Veuillez remplir tous les champs");
                return;
            }
            
            boolean success = db.registerPlayer(username, password, firstName, lastName, "default_avatar.png");
            
            if (success) {
                view.showMessage("Inscription réussie! Vous pouvez maintenant vous connecter.");
                view.close();
            } else {
                view.showMessage("Erreur lors de l'inscription. Le nom d'utilisateur existe peut-être déjà.");
            }
        }
    }

    class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
        }
    }
}