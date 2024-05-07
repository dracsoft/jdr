
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class joueur {
    private static JComboBox<String> professionComboBox;
    private static JComboBox<String> raceComboBox;
    private static DefaultTableModel table;
    private static String playerName;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    // Clé secrète pour le chiffrement AES (128 bits)
    private static final String SECRET_KEY = "amErIqAtRacERKey";

    // Fonction pour chiffrer une chaîne de caractères avec AES
    private static String encrypt(String strToEncrypt) {
        try {
            Key secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.out.println("Erreur de chiffrement: " + e.getMessage());
            return null;
        }
    }

    // Fonction pour déchiffrer une chaîne de caractères chiffrée avec AES
    private static String decrypt(String strToDecrypt, String decryptionKey) {
        try {
            Key secretKey = new SecretKeySpec(decryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Erreur de déchiffrement: " + e.getMessage());
            return null;
        }
    }

    private static void createAndShowGUI() {
        // Créer une nouvelle fenêtre
        JFrame frame = new JFrame("Caractéristiques");
        frame.setSize(600, 400); // Définir la taille de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermer l'application lorsque la fenêtre est fermée

        // Créer un panneau pour organiser les composants
        JPanel panel = new JPanel(new BorderLayout());

        // Créer une liste déroulante pour la profession
        String[] professions = { "Guerrier", "Bard", "Mage", "Prêtre", "Paladin", "Ranger" }; // ajouter le prof ici
                                                                                              // ligne 90 98 a ne pas
                                                                                              // oublier pour configuré
                                                                                              // la race de manier a
                                                                                              // rentre les valeur auto
        professionComboBox = new JComboBox<>(professions);

        // Créer une liste déroulante pour la race
        String[] races = { "Race 1", "Humain", "Race 3" }; // l ajout des race c est ici peux etre a voir pour ajouter
                                                           // les sous race dans un autre bouton apres avoir sélectionné
                                                           // la race principale
        raceComboBox = new JComboBox<>(races);

        // Créer un bouton pour saisir les valeurs
        JButton enterButton = new JButton("Entrer Valeurs");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterValues();
            }
        });
        // Créer un bouton pour enregistrer les données
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = JOptionPane.showInputDialog("Entrez le nom du joueur:");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    saveData(playerName);
                } else {
                    JOptionPane.showMessageDialog(null, "Nom du joueur invalide.");
                }
            }
        });
        // Créer un bouton pour ouvrir les données
        JButton openButton = new JButton("Ouvrir");
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = JOptionPane.showInputDialog("Entrez le nom du joueur:");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    loadData(playerName);
                } else {
                    JOptionPane.showMessageDialog(null, "Nom du joueur invalide.");
                }
            }
        });
        // Créer un bouton pour supprimer les données
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerName = JOptionPane.showInputDialog("Entrez le nom du joueur à supprimer:");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    deletePlayerWithKey(playerName);
                } else {
                    JOptionPane.showMessageDialog(null, "Nom du joueur invalide.");
                }
            }
        });

        // Créer un panneau pour les listes déroulantes et les boutons
        JPanel comboPanel = new JPanel();
        comboPanel.add(new JLabel("Profession: "));
        comboPanel.add(professionComboBox);
        comboPanel.add(new JLabel("Race: "));
        comboPanel.add(raceComboBox);
        comboPanel.add(enterButton);
        comboPanel.add(saveButton);
        comboPanel.add(openButton);
        comboPanel.add(deleteButton);
        panel.add(comboPanel, BorderLayout.NORTH);

        // tableaux des caractéristiques
        String[] columnNames = { "Caractéristique", "Valeur", "Valeur Normale", "Race", "Total" };
        Object[][] data = {
                { "Force", 0, 0, "", 0 },
                { "Agilité", 0, 0, "", 0 },
                { "Constitution", 0, 0, "", 0 },
                { "Intelligence", 0, 0, "", 0 },
                { "Intuition", 0, 0, "", 0 },
                { "Présence", 0, 0, "", 0 },
                { "Chance", 0, 0, "", 0 },
                { "Apparence", 0, 0, "", 0 },
        };
        table = new DefaultTableModel(data, columnNames);
        JTable jTable = new JTable(table);

        JScrollPane scrollPane = new JScrollPane(jTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Ajouter le panneau à la fenêtre
        frame.add(panel);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    }

    // Saisir les valeurs pour chaque caractéristique et les afficher dans le
    // tableau
    private static void enterValues() {
        for (int i = 0; i < table.getRowCount(); i++) {
            String characteristic = (String) table.getValueAt(i, 0);
            String valueStr = JOptionPane.showInputDialog("Entrez la valeur pour " + characteristic + ":");
            int value = Integer.parseInt(valueStr);
            table.setValueAt(value, i, 1); // Valeur

            String profession = (String) professionComboBox.getSelectedItem();
            int professionValue = 0;
            if (profession.equals("Guerrier")) {
                if (characteristic.equals("Force")) {
                    professionValue = 15;
                } else if (characteristic.equals("Agilité")) {
                    professionValue = 10;
                } else if (characteristic.equals("Constitution")) {
                    professionValue = 5;
                }
            } else if (profession.equals("Bard")) {
                if (characteristic.equals("Force")) {
                    professionValue = 10;
                } else if (characteristic.equals("Agilité")) {
                    professionValue = 5;
                } else if (characteristic.equals("Constitution")) {
                    professionValue = 10;
                } else if (characteristic.equals("Intelligence")) {
                    professionValue = 5;
                }
            } else if (profession.equals("Mage")) {
                if (characteristic.equals("Agilité")) {
                    professionValue = 5;
                } else if (characteristic.equals("Intelligence")) {
                    professionValue = 15;
                } else if (characteristic.equals("Intuition")) {
                    professionValue = 5;
                } else if (characteristic.equals("Présence")) {
                    professionValue = 5;
                }
            } else if (profession.equals("Prêtre")) {
                if (characteristic.equals("Force")) {
                    professionValue = 5;
                } else if (characteristic.equals("Agilité")) {
                    professionValue = 5;
                } else if (characteristic.equals("Intelligence")) {
                    professionValue = 5;
                } else if (characteristic.equals("Intuition")) {
                    professionValue = 15;
                }
            } else if (profession.equals("Paladin")) {
                if (characteristic.equals("Force")) {
                    professionValue = 10;
                } else if (characteristic.equals("Constitution")) {
                    professionValue = 10;
                } else if (characteristic.equals("Intuition")) {
                    professionValue = 5;
                } else if (characteristic.equals("Présence")) {
                    professionValue = 5;
                }
            } else if (profession.equals("Ranger")) {
                if (characteristic.equals("Force")) {
                    professionValue = 10;
                } else if (characteristic.equals("Agilité")) {
                    professionValue = 15;
                } else if (characteristic.equals("Constitution")) {
                    professionValue = 5;
                }
            }

            table.setValueAt(professionValue, i, 2); // Valeur Normale (Profession)

            String race = (String) raceComboBox.getSelectedItem();
            int raceValue = 0;
            if (race.equals("Humain") && characteristic.equals("Force")) { // metode pour ajouter auto un 5 en fo pour
                                                                           // les humain basique
                raceValue = 5;
            }
            table.setValueAt(raceValue, i, 3);

            // Calculer le total
            int total = value + professionValue + raceValue; // Valeur + Valeur Normale (Profession) + Race
            table.setValueAt(total, i, 4); // affiche le total
        }
    }

    // Fonction pour enregistrer les données chiffrées dans un fichier
    private static void saveData(String playerName) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                data.append(table.getValueAt(i, j));
                if (j < table.getColumnCount() - 1) {
                    data.append(",");
                }
            }
            data.append("\n");
        }
        String fileName = playerName + "_data.enc"; // Utilisez le nom du joueur pour créer un fichier unique
        String encryptedData = encrypt(data.toString());
        if (encryptedData != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(encryptedData);
                JOptionPane.showMessageDialog(null, "Données enregistrées avec succès pour " + playerName);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement des données");
                e.printStackTrace();
            }
        }
    }

    private static String requestDecryptionKey() {
        return JOptionPane.showInputDialog("Entrez la clé de décryptage :");
    }

    // Fonction pour charger les données chiffrées à partir d'un fichier et les
    // déchiffrer
    private static void loadData(String playerName) {
        String decryptionKey = requestDecryptionKey();
        if (decryptionKey != null && !decryptionKey.trim().isEmpty()) {
            String fileName = playerName + "_data.enc"; // Nom du fichier à partir duquel vous souhaitez charger les
                                                        // données
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String decryptedData = decrypt(stringBuilder.toString(), decryptionKey);
                if (decryptedData != null) {
                    JOptionPane.showMessageDialog(null, "Données chargées avec succès pour " + playerName);
                    // Traiter les données déchiffrées ici
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors du déchiffrement des données");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erreur lors du chargement des données");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Clé de décryptage invalide.");
        }
    }

    // Fonction pour supprimer les données d'un joueur après avoir vérifié la clé
    private static void deletePlayerWithKey(String playerName) {
        String inputKey = JOptionPane.showInputDialog("Entrez la clé pour supprimer " + playerName + ":");
        if (inputKey != null && inputKey.equals(SECRET_KEY)) {
            deleteData(playerName);
        } else {
            JOptionPane.showMessageDialog(null, "Clé incorrecte. Impossible de supprimer " + playerName);
        }
    }

    // Fonction pour supprimer les données d'un joueur
    private static void deleteData(String playerName) {
        String fileName = playerName + "_data.enc";
        File file = new File(fileName);
        if (file.exists()) {
            if (file.delete()) {
                JOptionPane.showMessageDialog(null, "Données supprimées avec succès pour " + playerName);
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression des données pour " + playerName);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Aucune donnée à supprimer pour " + playerName);
        }
    }

}
