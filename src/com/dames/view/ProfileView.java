package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProfileView extends JFrame {
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JTextField avatarField;
    private JButton saveButton;
    private JButton cancelButton;

    public ProfileView() {
        setTitle("Modifier le Profil");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        
        // Main panel with gradient background
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color1 = new Color(20, 30, 48);
                Color color2 = new Color(36, 59, 85);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title with animation
        JLabel titleLabel = new JLabel("Modifier le Profil", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 255, 255));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Animate title
        Timer titleTimer = new Timer(50, e -> {
            float hue = (System.currentTimeMillis() % 5000) / 5000f;
            titleLabel.setForeground(Color.getHSBColor(hue, 0.7f, 0.9f));
        });
        titleTimer.start();

        // Create styled text fields
        currentPasswordField = createStyledPasswordField();
        newPasswordField = createStyledPasswordField();
        confirmPasswordField = createStyledPasswordField();
        avatarField = createStyledTextField();

        // Current Password
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel currentPassLabel = createStyledLabel("Mot de passe actuel:");
        panel.add(currentPassLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(currentPasswordField, gbc);

        // New Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel newPassLabel = createStyledLabel("Nouveau mot de passe:");
        panel.add(newPassLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(newPasswordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel confirmPassLabel = createStyledLabel("Confirmer mot de passe:");
        panel.add(confirmPassLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(confirmPasswordField, gbc);

        // Avatar
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel avatarLabel = createStyledLabel("Nouvel avatar:");
        panel.add(avatarLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(avatarField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        ImageIcon saveIcon = new ImageIcon(new ImageIcon("assets/save.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        ImageIcon cancelIcon = new ImageIcon(new ImageIcon("assets/cancel.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        
        saveButton = createStyledButton("Enregistrer", new Color(76, 175, 80), saveIcon);
        cancelButton = createStyledButton("Annuler", new Color(244, 67, 54), cancelIcon);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(220, 220, 220));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw border
                g2.setColor(new Color(255, 255, 255, 100));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(Color.WHITE);
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        textField.setCaretColor(Color.WHITE);
        textField.setPreferredSize(new Dimension(250, 35));
        
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Draw border
                g2.setColor(new Color(255, 255, 255, 100));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(Color.WHITE);
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setPreferredSize(new Dimension(250, 35));
        
        return passwordField;
    }

    private JButton createStyledButton(String text, Color bgColor, ImageIcon icon) {
        JButton button = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(15);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        addButtonHoverEffect(button, bgColor);
        
        return button;
    }

    private void addButtonHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                animateButtonColor(button, baseColor, baseColor.brighter(), 150);
                button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                animateButtonColor(button, baseColor.brighter(), baseColor, 150);
                button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
        });
    }

    private void animateButtonColor(JButton button, Color from, Color to, int duration) {
        new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < duration) {
                    float progress = (float)(System.currentTimeMillis() - startTime) / duration;
                    int red = (int)(from.getRed() + (to.getRed() - from.getRed()) * progress);
                    int green = (int)(from.getGreen() + (to.getGreen() - from.getGreen()) * progress);
                    int blue = (int)(from.getBlue() + (to.getBlue() - from.getBlue()) * progress);
                    button.setBackground(new Color(red, green, blue));
                    button.repaint();
                    Thread.sleep(10);
                }
                button.setBackground(to);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Getters pour les champs
    public char[] getCurrentPassword() {
        return currentPasswordField.getPassword();
    }

    public char[] getNewPassword() {
        return newPasswordField.getPassword();
    }

    public char[] getConfirmPassword() {
        return confirmPasswordField.getPassword();
    }

    public String getAvatar() {
        return avatarField.getText();
    }

    // Méthodes pour ajouter des listeners
    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearFields() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        avatarField.setText("");
    }
}