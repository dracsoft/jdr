
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class joueur {
    private static JComboBox<String> professionComboBox;
    private static JComboBox<String> raceComboBox;
    private static DefaultTableModel characteristicTableModel;
    private static DefaultTableModel skillTableModel;
    private static DefaultTableModel WeaponsTableModel;
    private static String playerName;
    // Variables pour les caractéristiques spécifiques à la race
    private static int raceForceValue = 0;
    private static int raceAgiliteValue = 0;
    private static int raceConstitutionValue = 0;
    private static int raceIntelligenceValue = 0;
    private static int raceIntuitionValue = 0;
    private static int racePresenceValue = 0;
    private static int raceChanceValue = 0;
    private static int raceApparenceValue = 0;
    private static JComboBox<String> subRaceComboBox;

    private static String requestDecryptionKey() {
        return JOptionPane.showInputDialog("Entrez la clé de décryptage :");
    }

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
        frame.setSize(1000, 600); // Définir la taille de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermer l'application lorsque la fenêtre est fermée

        // Créer un panneau pour organiser les composants
        JPanel panel = new JPanel(new BorderLayout());

        String[] subRaces = { "Béornides", "Numenoréens Noirs", "Corsaires", "Dorwinrim", "Dunedain", "Dunelendings",
                "Easterlinges", "Haradrim", "Lossoth", "Rohirim", "Ruraux", "Urbains", "Variags", "Homme des bois",
                "Woses" };
        String[] subRaces2 = { "Semi-Elfe", "Elfe Sylvain", "Elfe Sindar", "Elfe Noldor" };
        String[] subRaces3 = { "aucun pour Hobbit" };
        String[] subRaces4 = { "aucun pour Umli" };
        String[] subRaces5 = { "aucun pour Nain" };

        // Créer une liste déroulante pour la profession
        String[] professions = { "Guerrier", "Bard", "Mage", "Prêtre", "Paladin", "Ranger" };
        professionComboBox = new JComboBox<>(professions);
        String[] races = { "Nain", "Humain", "Elfe", "Hobbit", "Umli" };
        raceComboBox = new JComboBox<>(races);
        subRaceComboBox = new JComboBox<>();
        subRaceComboBox.setEnabled(false);

        raceComboBox.addActionListener(e -> {
            String selectedRace = (String) raceComboBox.getSelectedItem();
            if (selectedRace.equals("Humain")) {
                subRaceComboBox.setModel(new DefaultComboBoxModel<>(subRaces));
            } else if (selectedRace.equals("Elfe")) {
                subRaceComboBox.setModel(new DefaultComboBoxModel<>(subRaces2));
            } else if (selectedRace.equals("Hobbit")) {
                subRaceComboBox.setModel(new DefaultComboBoxModel<>(subRaces3));
            } else if (selectedRace.equals("Umli")) {
                subRaceComboBox.setModel(new DefaultComboBoxModel<>(subRaces4));
            } else if (selectedRace.equals("Nain")) {
                subRaceComboBox.setModel(new DefaultComboBoxModel<>(subRaces5));
            }
            subRaceComboBox.setEnabled(true);
        });

        // Créer un bouton pour saisir les valeurs
        JButton enterButton = new JButton("Entrer Valeurs");
        enterButton.addActionListener(e -> enterValues());

        // Créer un bouton pour enregistrer les données
        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            playerName = JOptionPane.showInputDialog("Entrez le nom du joueur:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                saveData(playerName);
            } else {
                JOptionPane.showMessageDialog(null, "Nom du joueur invalide.");
            }
        });

        // Créer un bouton pour ouvrir les données
        JButton openButton = new JButton("Ouvrir");
        openButton.addActionListener(e -> {
            playerName = JOptionPane.showInputDialog("Entrez le nom du joueur:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                loadData(playerName);
            } else {
                JOptionPane.showMessageDialog(null, "Nom du joueur invalide.");
            }
        });

        // Créer un bouton pour supprimer les données
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(e -> {
            playerName = JOptionPane.showInputDialog("Entrez le nom du joueur à supprimer:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                deletePlayerWithKey(playerName);
            } else {
                JOptionPane.showMessageDialog(null, "Nom du joueur invalide.");
            }
        });

        // Créer un panneau pour les listes déroulantes et les boutons
        JPanel comboPanel = new JPanel(new GridLayout(2, 1));
        JPanel subComboPanel = new JPanel(new FlowLayout());
        subComboPanel.add(new JLabel("Profession: "));
        subComboPanel.add(professionComboBox);
        subComboPanel.add(new JLabel("Race: "));
        subComboPanel.add(raceComboBox);
        subComboPanel.add(new JLabel("Sous-race: "));
        subComboPanel.add(subRaceComboBox);
        comboPanel.add(subComboPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(enterButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(openButton);
        buttonPanel.add(deleteButton);
        comboPanel.add(buttonPanel);

        panel.add(comboPanel, BorderLayout.NORTH);

        // Tableau pour les caractéristiques
        String[] columnNamesCharacteristics = { "Caractéristique", "Valeur", "Valeur Normale", "Race", "Total" };
        Object[][] dataCharacteristics = {
                { "Force", 0, 0, "", 0 },
                { "Agilité", 0, 0, "", 0 },
                { "Constitution", 0, 0, "", 0 },
                { "Intelligence", 0, 0, "", 0 },
                { "Intuition", 0, 0, "", 0 },
                { "Présence", 0, 0, "", 0 },
                { "Chance", 0, 0, "", 0 },
                { "Apparence", 0, 0, "", 0 },
        };
        characteristicTableModel = new DefaultTableModel(dataCharacteristics, columnNamesCharacteristics);
        JTable characteristicTable = new JTable(characteristicTableModel);
        JScrollPane characteristicScrollPane = new JScrollPane(characteristicTable);

        // Tableau pour les compétences de mouvement et les types d'armure
        String[] columnNamesSkills = { "Manoeuvre/Mouvement", "Degré", "Prof", "carac", "Object", "Spéc", "Spéc2",
                "Total" };
        Object[][] dataSkills = {
                { "Sans Armure", "", "xx", "", "", "", 0, "" },
                { "Cuir Souple", "", "xx", "", "", "", -15, "" },
                { "Cuir Rigide", "", "xx", "", "", "", -30, "" },
                { "Cotte de Maille", "", "xx", "", "", "", -45, "" },
                { "Plate", "", "xx", "", "", "", -60, "" },
        };
        skillTableModel = new DefaultTableModel(dataSkills, columnNamesSkills);
        JTable skillTable = new JTable(skillTableModel);
        JScrollPane skillScrollPane = new JScrollPane(skillTable);

        // Tableau pour les armes (bonus offensifs)
        String[] columnWeaponsNamesSkills = { "Armes(bonus offensifs)", "Degré", "Prof", "carac", "Object", "Spéc",
                "Spéc2", "Total" };
        Object[][] dataWeapons = {
                { "Tranchant à 1 main", "", "xx", "", "", "", "", "" },
                { "Contandant à 1 main", "", "xx", "", "", "", "", "" },
                { "à 2 main", "", "xx", "", "", "", "", "" },
                { "Lancer", "", "xx", "", "", "", "", "" },
                { "Projectile", "", "xx", "", "", "", "", "" },
                { "D'Hast", "", "xx", "", "", "", "", "" },
        };
        WeaponsTableModel = new DefaultTableModel(dataWeapons, columnWeaponsNamesSkills);
        JTable weaponsTable = new JTable(WeaponsTableModel);
        JScrollPane weaponsScrollPane = new JScrollPane(weaponsTable);

        // Panneau pour les tableaux
        JPanel tablePanel = new JPanel(new GridLayout(3, 1));
        tablePanel.add(characteristicScrollPane);
        tablePanel.add(skillScrollPane);
        tablePanel.add(weaponsScrollPane);
        panel.add(tablePanel, BorderLayout.CENTER);

        // Ajouter le panneau à la fenêtre
        frame.add(panel);

        // Rendre la fenêtre visible
        frame.setVisible(true);
    } // Saisir les valeurs pour chaque caractéristique et les afficher dans le
      // tableau
      // Saisir les valeurs pour chaque caractéristique et les afficher dans le
      // tableau
      // Modifier la méthode enterValues

    private static void updateSubRaceValues(String race, String subRace, String subRace2, String subRace3,
            String subRace4, String subRace5) {
        // Vérifiez si la variable "race" est nulle avant de l'utiliser
        if (race != null) {
            switch (race) {
                case "Hobbit":
                    switch (subRace5) {
                        default:
                            raceForceValue = -20;
                            raceAgiliteValue = 15;
                            raceConstitutionValue = 15;
                            raceIntelligenceValue = 0;
                            raceIntuitionValue = -5;
                            racePresenceValue = -5;
                            raceChanceValue = 0;
                            raceApparenceValue = 0;
                            break;
                    }
                    break;
                case "Umli":
                    switch (subRace4) {
                        default:
                            raceForceValue = 5;
                            raceAgiliteValue = 0;
                            raceConstitutionValue = 10;
                            raceIntelligenceValue = 0;
                            raceIntuitionValue = -5;
                            racePresenceValue = -5;
                            raceChanceValue = 0;
                            raceApparenceValue = 0;
                            break;
                    }
                case "Nain":
                    switch (subRace3) {
                        default:
                            raceForceValue = 5;
                            raceAgiliteValue = -5;
                            raceConstitutionValue = 15;
                            raceIntelligenceValue = 0;
                            raceIntuitionValue = -5;
                            racePresenceValue = -5;
                            raceChanceValue = 0;
                            raceApparenceValue = 0;
                            break;
                    }
                case "Humain":
                    // Traitement pour les Humains
                    switch (subRace) {
                        case "Dunedain":
                            raceForceValue = 5;
                            raceConstitutionValue = 10;
                            racePresenceValue = 5;
                            break;
                        case "Woses":
                            raceConstitutionValue = 10;
                            racePresenceValue = -5;
                            break;
                        default:
                            // Pour toutes les autres sous-races
                            raceForceValue = 5;
                            break;
                    }
                    break;
                case "Elfe":
                    // Traitement pour les Elfes
                    switch (subRace2) {
                        case "Semi-Elfe":
                            raceForceValue = 5;
                            raceAgiliteValue = 5;
                            raceConstitutionValue = 5;
                            raceIntelligenceValue = 0;
                            raceIntuitionValue = 0;
                            racePresenceValue = 5;
                            raceChanceValue = 0;
                            raceApparenceValue = 0;
                            break;
                        case "Elfe Sylvain":
                            raceForceValue = 0;
                            raceAgiliteValue = 10;
                            raceConstitutionValue = 0;
                            raceIntelligenceValue = 0;
                            raceIntuitionValue = 5;
                            racePresenceValue = 5;
                            raceChanceValue = 0;
                            raceApparenceValue = 0;
                            break;
                        case "Elfe Sindar":
                            raceForceValue = 0;
                            raceAgiliteValue = 10;
                            raceConstitutionValue = 5;
                            raceIntelligenceValue = 0;
                            raceIntuitionValue = 5;
                            racePresenceValue = 10;
                            raceChanceValue = 0;
                            raceApparenceValue = 0;
                            break;
                        case "Elfe Noldor":
                            raceForceValue = 0;
                            raceAgiliteValue = 15;
                            raceConstitutionValue = 10;
                            raceIntelligenceValue = 5;
                            raceIntuitionValue = 5;
                            racePresenceValue = 15;
                            raceChanceValue = 0;
                            raceApparenceValue = 0;
                            break;
                    }
                    break;
                // Ajoutez d'autres cas pour d'autres races si nécessaire
            }
        }
    }

    private static void enterValues() {
        // Options pour le dialogue
        String[] options = { "Caractéristiques de la race", "Nombre de cases pour les compétences", "stat arme" };

        // Affichage du dialogue pour choisir entre caractéristiques ou compétences
        int choice = JOptionPane.showOptionDialog(null, "Que voulez-vous entrer ?", "Choix",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            // Entrée des caractéristiques de la race
            String race = (String) raceComboBox.getSelectedItem();
            String subRace = (String) subRaceComboBox.getSelectedItem();
            updateSubRaceValues(race, subRace, subRace, subRace, subRace, subRace); // Mise à jour des valeurs
                                                                                    // spécifiques à la race

            for (int i = 0; i < characteristicTableModel.getRowCount(); i++) {
                String characteristic = (String) characteristicTableModel.getValueAt(i, 0);
                String valueStr = JOptionPane.showInputDialog("Entrez la valeur pour " + characteristic + ":");
                int value = Integer.parseInt(valueStr);
                characteristicTableModel.setValueAt(value, i, 1); // Valeur

                // Mise à jour des valeurs spécifiques à la profession
                String profession = (String) professionComboBox.getSelectedItem();
                int professionValue = calculateProfessionValue(profession, characteristic);
                characteristicTableModel.setValueAt(professionValue, i, 2);

                // Mise à jour des valeurs spécifiques à la race
                int raceValue = getRaceValue(characteristic);
                characteristicTableModel.setValueAt(raceValue, i, 3);

                // Calculer le total pour cette ligne
                int totalValue = value + professionValue + raceValue;
                characteristicTableModel.setValueAt(totalValue, i, 4);
            }
        } else if (choice == 1) {
            updateSkills();
        } else if (choice == 2) {
            updateWeapons();
        }
    }

    private static void updateSkills() {
        // Récupérer les valeurs totales des caractéristiques
        int totalForce = (int) characteristicTableModel.getValueAt(0, 4); // Total de la force
        int totalAgilite = (int) characteristicTableModel.getValueAt(1, 4); // Total de l'agilité
        // Utiliser une méthode pour mettre à jour les valeurs en fonction des indices
        // de ligne
        caracSkillAttributes(totalForce, totalAgilite);

        // Récupérer la sous-race sélectionnée
        String subRace = (String) subRaceComboBox.getSelectedItem();

        if (subRace != null) {
            // Étape 1 : Demander le nombre de cases pour chaque compétence et calculer le
            // totalDegree
            for (int i = 0; i < skillTableModel.getRowCount(); i++) {
                String skillName = (String) skillTableModel.getValueAt(i, 0);

                // Obtenir le degré de base en fonction de la sous-race et du nom de la
                // compétence
                int baseDegree = BaseDegreeForSkill(subRace, skillName);

                // Demander le nombre de cases
                int numCases = Integer
                        .parseInt(JOptionPane.showInputDialog("Entrez le nombre de cases pour " + skillName + ":"));

                // Calculer le total du degré en fonction du degré de base et du nombre de cases
                int totalDegree = (baseDegree + numCases) * 5;

                // Mettre à jour le modèle avec le total du degré ou -25 si le total est zéro
                if (totalDegree != 0) {
                    skillTableModel.setValueAt(String.valueOf(totalDegree), i, 1);
                } else {
                    skillTableModel.setValueAt("-25", i, 1);
                }
            }

            // Étape 2 : Demander les bonus et spécificités, puis recalculer les compétences
            for (int i = 0; i < skillTableModel.getRowCount(); i++) {
                String skillName = (String) skillTableModel.getValueAt(i, 0);
                // Demander s'il y a des bonus ou des spécificités
                int response = JOptionPane.showOptionDialog(null,
                        "Y a-t-il des bonus ou des spécificités pour " + skillName + "?",
                        "Bonus et Spécificités",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[] { "Oui", "Non" },
                        "Non");

                if (response == JOptionPane.YES_OPTION) {
                    // Demander et mettre à jour le bonus d'objet spécial
                    String objectBonusStr = JOptionPane
                            .showInputDialog("Entrez le bonus d'objet spécial pour " + skillName + ":");
                    int objectBonus = 0;
                    if (objectBonusStr != null && !objectBonusStr.trim().isEmpty()) {
                        try {
                            objectBonus = Integer.parseInt(objectBonusStr);
                        } catch (NumberFormatException e) {
                            objectBonus = 0; // Mettre une valeur par défaut en cas d'erreur de format
                        }
                    }
                    skillTableModel.setValueAt(String.valueOf(objectBonus), i, 4);

                    // Demander et mettre à jour le bonus de spécificité
                    String specBonusStr = JOptionPane
                            .showInputDialog("Entrez le bonus de spécificité pour " + skillName + ":");
                    int specBonus = 0;
                    if (specBonusStr != null && !specBonusStr.trim().isEmpty()) {
                        try {
                            specBonus = Integer.parseInt(specBonusStr);
                        } catch (NumberFormatException e) {
                            specBonus = 0; // Mettre une valeur par défaut en cas d'erreur de format
                        }
                    }
                    skillTableModel.setValueAt(String.valueOf(specBonus), i, 5);
                } else if (response == JOptionPane.NO_OPTION) {
                    // Si la réponse est "Non", mettre les bonus à 0
                    skillTableModel.setValueAt("0", i, 4); // Bonus d'objet spécial
                    skillTableModel.setValueAt("0", i, 5); // Bonus de spécificité
                }

                // Calculer et mettre à jour le total de la compétence
                int totalSkill;
                int totalDegree = Integer.parseInt((String) skillTableModel.getValueAt(i, 1)); // Récupérer le
                                                                                               // totalDegree déjà
                                                                                               // calculé
                String caracValue = (String) skillTableModel.getValueAt(i, 3); // Récupère la valeur de la colonne
                                                                               // "carac"
                int specBonus2 = Integer.parseInt(skillTableModel.getValueAt(i, 6).toString()); // Récupérer le
                                                                                                // specBonus2

                // Récuif (caracValue.startsWith("érer les valeurs actuelles des bonus
                int objectBonus = Integer.parseInt((String) skillTableModel.getValueAt(i, 4));
                int specBonus = Integer.parseInt((String) skillTableModel.getValueAt(i, 5));

                if (caracValue.startsWith("AG")) {
                    totalSkill = totalDegree + totalAgilite + objectBonus + specBonus + specBonus2;
                } else if (caracValue.startsWith("FO")) {
                    totalSkill = totalDegree + totalForce + objectBonus + specBonus + specBonus2;
                } else {
                    totalSkill = totalDegree + objectBonus + specBonus + specBonus2; // Cas par défaut
                }
                skillTableModel.setValueAt(String.valueOf(totalSkill), i, 7); // Mettre à jour la colonne "totalSkill"
            }
        }
    }

    private static void updateWeapons() {
        // Récupérer les valeurs totales des caractéristiques pour les armes
        int totalForce = (int) characteristicTableModel.getValueAt(0, 4); // Total de la force
        int totalAgilite = (int) characteristicTableModel.getValueAt(1, 4); // Total de l'agilité

        // Utiliser une méthode pour mettre à jour les valeurs en fonction des indices
        // de ligne
        updateWeaponsAttributes(totalForce, totalAgilite);

        // Récupérer la sous-race sélectionnée
        String subRace = (String) subRaceComboBox.getSelectedItem();

        if (subRace != null) {
            // Parcourir le tableau de données des armes
            for (int i = 0; i < WeaponsTableModel.getRowCount(); i++) {
                String weaponName = (String) WeaponsTableModel.getValueAt(i, 0); // Nom de l'arme

                // Obtenir le degré de base pour l'arme
                int baseDegree = BaseDegreeForWeapons(subRace, weaponName);

                // Demander le nombre de cases pour l'arme
                int numCases = Integer
                        .parseInt(JOptionPane.showInputDialog("Entrez le nombre de cases pour " + weaponName + ":"));

                // Calculer le total du degré en fonction du degré de base et du nombre de cases
                int totalDegree = (baseDegree + numCases) * 5;

                // Mettre à jour le modèle avec le total du degré ou -25 si le total est zéro
                if (totalDegree != 0) {
                    WeaponsTableModel.setValueAt(String.valueOf(totalDegree), i, 1);
                } else {
                    WeaponsTableModel.setValueAt("-25", i, 1);
                }

                // Demander s'il y a des bonus ou des spécificités pour l'arme
                int response = JOptionPane.showOptionDialog(null,
                        "Y a-t-il des bonus ou des spécificités pour " + weaponName + "?",
                        "Bonus et Spécificités",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[] { "Oui", "Non" },
                        "Non");

                if (response == JOptionPane.YES_OPTION) {
                    // Demander et mettre à jour le bonus d'objet spécial
                    String objectBonusStr = JOptionPane
                            .showInputDialog("Entrez le bonus d'objet spécial pour " + weaponName + ":");
                    int objectBonus = 0;
                    if (objectBonusStr != null && !objectBonusStr.trim().isEmpty()) {
                        try {
                            objectBonus = Integer.parseInt(objectBonusStr);
                        } catch (NumberFormatException e) {
                            objectBonus = 0; // Mettre une valeur par défaut en cas d'erreur de format
                        }
                    }
                    WeaponsTableModel.setValueAt(String.valueOf(objectBonus), i, 4);

                    // Demander et mettre à jour le bonus de spécificité
                    String specBonusStr = JOptionPane
                            .showInputDialog("Entrez le bonus de spécificité pour " + weaponName + ":");
                    int specBonus = 0;
                    if (specBonusStr != null && !specBonusStr.trim().isEmpty()) {
                        try {
                            specBonus = Integer.parseInt(specBonusStr);
                        } catch (NumberFormatException e) {
                            specBonus = 0; // Mettre une valeur par défaut en cas d'erreur de format
                        }

                    }
                    WeaponsTableModel.setValueAt(String.valueOf(specBonus), i, 5);

                    // Demander et mettre à jour le bonus de spécificité
                    String spec2BonusStr = JOptionPane
                            .showInputDialog("Entrez le bonus de spécificité pour " + weaponName + ":");
                    int spec2Bonus = 0;
                    if (spec2BonusStr != null && !spec2BonusStr.trim().isEmpty()) {
                        try {
                            spec2Bonus = Integer.parseInt(spec2BonusStr);
                        } catch (NumberFormatException e) {
                            spec2Bonus = 0; // Mettre une valeur par défaut en cas d'erreur de format
                        }

                    }
                    WeaponsTableModel.setValueAt(String.valueOf(spec2Bonus), i, 6);

                } else if (response == JOptionPane.NO_OPTION) {
                    // Si la réponse est "Non", mettre les bonus à 0
                    WeaponsTableModel.setValueAt("0", i, 4); // Bonus d'objet spécial
                    WeaponsTableModel.setValueAt("0", i, 5); // Bonus de spécificité
                    WeaponsTableModel.setValueAt("0", i, 6); // Bonus de spécificité2
                }

                // Calculer et mettre à jour le total de la compétence pour l'arme
                int totalWeapon;
                int totalDegreeWeapon = Integer.parseInt((String) WeaponsTableModel.getValueAt(i, 1)); // Récupérer le
                                                                                                       // totalDegree
                                                                                                       // déjà calculé
                String caracValue = (String) WeaponsTableModel.getValueAt(i, 3); // Récupérer la valeur de la colonne //
                                                                                 // "carac"
                int objectBonus = Integer.parseInt((String) WeaponsTableModel.getValueAt(i, 4));
                int specBonus = Integer.parseInt((String) WeaponsTableModel.getValueAt(i, 5));
                int specBonus2 = Integer.parseInt((String) WeaponsTableModel.getValueAt(i, 6)); // Récupérer le
                // specBonus2

                // Calculer le totalSkill en fonction de la valeur de caracValue
                if (caracValue.startsWith("AG")) {
                    totalWeapon = totalDegreeWeapon + totalAgilite + objectBonus + specBonus + specBonus2;
                } else if (caracValue.startsWith("FO")) {
                    totalWeapon = totalDegreeWeapon + totalForce + objectBonus + specBonus + specBonus2;
                } else {
                    totalWeapon = totalDegreeWeapon + objectBonus + specBonus + specBonus2; // Cas par défaut
                }

                // Mettre à jour la colonne "totalSkill" dans le modèle de tableau des armes
                WeaponsTableModel.setValueAt(String.valueOf(totalWeapon), i, 7);
            }
        }
        // Mettre à jour l'affichage ou le modèle de tableau pour les armes si
        // nécessaire
    }

    private static void caracSkillAttributes(int totalForce, int totalAgilite) {
        for (int z = 0; z < skillTableModel.getRowCount(); z++) {
            if (z < 3) {
                skillTableModel.setValueAt("AG:" + totalAgilite, z, 3); // Total de l'agilité pour les 2 premières
                                                                        // lignes
            } else if (z < 6) {
                skillTableModel.setValueAt("FO:" + totalForce, z, 3); // Total de la force pour les lignes 3 à 5
            }
        }
    }

    private static void updateWeaponsAttributes(int totalForce, int totalAgilite) {
        for (int z = 0; z < WeaponsTableModel.getRowCount(); z++) {
            if (z < 3 || z == 5) {
                WeaponsTableModel.setValueAt("FO:" + totalForce, z, 3); // Total de la force pour les lignes 3 à 5
                                                                        // lignes
            } else if (z == 3 || z == 4) {
                WeaponsTableModel.setValueAt("AG:" + totalAgilite, z, 3); // Total de l'agilité pour les 2 premières
            }
        }
    }

    private static int calculateProfessionValue(String profession, String characteristic) {
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
        return professionValue;
    }

    private static int getRaceValue(String characteristic) {
        switch (characteristic) {
            case "Force":
                return raceForceValue;
            case "Agilité":
                return raceAgiliteValue;
            case "Constitution":
                return raceConstitutionValue;
            case "Intelligence":
                return raceIntelligenceValue;
            case "Intuition":
                return raceIntuitionValue;
            case "Présence":
                return racePresenceValue;
            case "Chance":
                return raceChanceValue;
            case "Apparence":
                return raceApparenceValue;
            default:
                return 0;
        }
    }

    private static int BaseDegreeForSkill(String subRace, String skillName) {
        int baseDegree = 0;
        switch (subRace) {
            case "aucun pour Hobbit":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;

                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "aucun pour Umli":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 3;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 3;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "aucun pour Nain":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 1;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 3;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Béornides":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Numenoréens Noirs":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 2;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Corsaires":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 2;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Dorwinrim":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 1;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Dunedain":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 1;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 2;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Dunelendings":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 1;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Easterlinges":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Haradrim":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 1;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Lossoth":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 3;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Rohirim":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 1;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 2;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Ruraux":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 1;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 1;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 1;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Urbains":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Variags":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 1;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Homme des bois":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 1;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Woses":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 3;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Semi-Elfe":
                switch (skillName) // cest stat sont bonne
                {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 1;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 1;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Elfe Sylvain":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Elfe Sindar":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Elfe Noldor":
                switch (skillName) {
                    case "Sans Armure":
                        baseDegree = 1;
                        break;
                    case "Cuir Souple":
                        baseDegree = 0;
                        break;
                    case "Cuir Rigide":
                        baseDegree = 0;
                        break;
                    case "Cotte de Maille":
                        baseDegree = 0;
                        break;
                    case "Plate":
                        baseDegree = 0;
                        break;
                }
                break;
        }
        return baseDegree;
    }

    private static int BaseDegreeForWeapons(String subRace, String weaponName) {
        int baseDegree = 0;
        switch (subRace) {
            case "aucun pour Hobbit":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 0;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 2;
                        break;
                    case "Projectile":
                        baseDegree = 2;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "aucun pour Umli":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 0;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 3;
                        break;
                    case "à 2 main":
                        baseDegree = 1;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 0;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "aucun pour Nain":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 0;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 4;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 0;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Béornides":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 0;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 1;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 0;
                        break;
                    case "D'Hast":
                        baseDegree = 2;
                        break;
                }
                break;
            case "Numenoréens Noirs":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Corsaires":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 2;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Dorwinrim":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 0;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 1;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 1;
                        break;
                }
                break;
            case "Dunedain":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 2;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 1;
                        break;
                    case "Lancer":
                        baseDegree = 0;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 1;
                        break;
                }
                break;
            case "Dunelendings":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 0;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 1;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 2;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 2;
                        break;
                }
                break;
            case "Easterlinges":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 2;
                        break;
                    case "D'Hast":
                        baseDegree = 2;
                        break;
                }
                break;
            case "Haradrim":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 0;
                        break;
                    case "D'Hast":
                        baseDegree = 2;
                        break;
                }
                break;
            case "Lossoth":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 0;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 3;
                        break;
                    case "Projectile":
                        baseDegree = 0;
                        break;
                    case "D'Hast":
                        baseDegree = 2;
                        break;
                }
                break;
            case "Rohirim":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 2;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 0;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 1;
                        break;
                }
                break;
            case "Ruraux":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 1;
                        break;
                }
                break;
            case "Urbains":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 0;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Variags":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 2;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 1;
                        break;
                }
                break;
            case "Homme des bois":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 1;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 1;
                        break;
                }
                break;
            case "Woses":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 2;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 4;
                        break;
                    case "Projectile":
                        baseDegree = 0;
                        break;
                    case "D'Hast":
                        baseDegree = 1;
                        break;
                }
                break;
            case "Semi-Elfe":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 0;
                        break;
                    case "Projectile":
                        baseDegree = 2;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Elfe Sylvain":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 0;
                        break;
                    case "Projectile":
                        baseDegree = 3;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Elfe Sindar":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 0;
                        break;
                    case "Projectile":
                        baseDegree = 2;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
            case "Elfe Noldor":
                switch (weaponName) {
                    case "Tranchant à 1 main":
                        baseDegree = 1;
                        break;
                    case "Contandant à 1 main":
                        baseDegree = 0;
                        break;
                    case "à 2 main":
                        baseDegree = 0;
                        break;
                    case "Lancer":
                        baseDegree = 0;
                        break;
                    case "Projectile":
                        baseDegree = 1;
                        break;
                    case "D'Hast":
                        baseDegree = 0;
                        break;
                }
                break;
        }
        return baseDegree;
    }

    private static void saveData(String playerName) {
        StringBuilder data = new StringBuilder();

        // Sauvegarde des données du premier tableau (caractéristiques)
        for (int i = 0; i < characteristicTableModel.getRowCount(); i++) {
            for (int j = 0; j < characteristicTableModel.getColumnCount(); j++) {
                data.append(characteristicTableModel.getValueAt(i, j));
                if (j < characteristicTableModel.getColumnCount() - 1) {
                    data.append(",");
                }
            }
            data.append("\n");
        }

        // Ajout d'une séparation entre les deux tableaux pour faciliter le traitement
        // lors du chargement
        data.append("#\n");

        // Sauvegarde des données du deuxième tableau (compétences)
        for (int i = 0; i < skillTableModel.getRowCount(); i++) {
            for (int j = 0; j < skillTableModel.getColumnCount(); j++) {
                data.append(skillTableModel.getValueAt(i, j));
                if (j < skillTableModel.getColumnCount() - 1) {
                    data.append(",");
                }
            }
            data.append("\n");
        }

        // Ajout d'une autre séparation pour le nouveau tableau
        data.append("#\n");

        // Sauvegarde des données du troisième tableau (WeaponsTableModel)
        for (int i = 0; i < WeaponsTableModel.getRowCount(); i++) {
            for (int j = 0; j < WeaponsTableModel.getColumnCount(); j++) {
                data.append(WeaponsTableModel.getValueAt(i, j));
                if (j < WeaponsTableModel.getColumnCount() - 1) {
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

    private static void loadData(String playerName) {
        // Demander la clé de déchiffrement
        String decryptionKey = requestDecryptionKey();
        if (decryptionKey == null || decryptionKey.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Clé de déchiffrement invalide.");
            return;
        }

        // Lire les données chiffrées depuis le fichier
        String fileName = playerName + "_data.enc";
        String encryptedData = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            encryptedData = stringBuilder.toString();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement des données");
            e.printStackTrace();
            return;
        }

        // Déchiffrer les données
        String decryptedData = decrypt(encryptedData, decryptionKey);
        if (decryptedData != null) {
            // Mettre à jour les modèles de tableau avec les données chargées
            String[] tablesData = decryptedData.split("#"); // Séparer les données des différents tableaux
            if (tablesData.length >= 1) { // Vérifier si le premier tableau est présent
                String[] characteristicsData = tablesData[0].split("\n");
                int startIndex = 0;
                if (characteristicsData.length > 0 && characteristicsData[0].trim().isEmpty()) {
                    startIndex = 1; // Ignorer la première ligne si elle est vide
                }
                for (int i = startIndex; i < characteristicsData.length
                        && i < characteristicTableModel.getRowCount(); i++) {
                    String[] values = characteristicsData[i].split(",");
                    for (int j = 0; j < values.length && j < characteristicTableModel.getColumnCount(); j++) {
                        characteristicTableModel.setValueAt(values[j], i - startIndex, j);
                    }
                }
            }
            if (tablesData.length >= 2) { // Vérifier si le deuxième tableau est présent
                String[] skillsData = tablesData[1].split("\n");
                int startIndex = 0;
                if (skillsData.length > 0 && skillsData[0].trim().isEmpty()) {
                    startIndex = 1; // Ignorer la première ligne si elle est vide
                }
                int numRows = Math.min(skillsData.length, skillTableModel.getRowCount());
                for (int i = startIndex; i < numRows + 1; i++) {
                    String[] values = skillsData[i].split(",");
                    for (int j = 0; j < values.length && j < skillTableModel.getColumnCount(); j++) {
                        skillTableModel.setValueAt(values[j], i - startIndex, j);
                    }
                }
            }
            if (tablesData.length >= 3) { // Vérifier si le troisième tableau est présent
                String[] weaponsData = tablesData[2].split("\n");
                int startIndex = 0;
                if (weaponsData.length > 0 && weaponsData[0].trim().isEmpty()) {
                    startIndex = 1; // Ignorer la première ligne si elle est vide
                }
                int numRows = Math.min(weaponsData.length, WeaponsTableModel.getRowCount());
                for (int i = startIndex; i < numRows + 1; i++) {
                    String[] values = weaponsData[i].split(",");
                    for (int j = 0; j < values.length && j < WeaponsTableModel.getColumnCount(); j++) {
                        WeaponsTableModel.setValueAt(values[j], i - startIndex, j);
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "Données chargées avec succès pour " + playerName);
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors du déchiffrement des données");
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

    // amErIqAtRacERKey
}
