import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class joueur {
    private static JComboBox<String> professionComboBox;
    private static JComboBox<String> raceComboBox;
    private static DefaultTableModel table;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        // Créer une nouvelle fenêtre
        JFrame frame = new JFrame("Caractéristiques");
        frame.setSize(600, 400); // Définir la taille de la fenêtre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermer l'application lorsque la fenêtre est fermée

        // Créer un panneau pour organiser les composants
        JPanel panel = new JPanel(new BorderLayout());

        // Créer une liste déroulante pour la profession
        String[] professions = {"Guerrier", "Bard","Mage","Prêtre","Paladin","Ranger"}; // ajouter le prof ici ligne 90  98 a ne pas oublier pour configuré la race de manier a rentre les valeur auto
        professionComboBox = new JComboBox<>(professions);

        // Créer une liste déroulante pour la race
        String[] races = {"Race 1", "Humain", "Race 3"}; // l ajout des race c est ici peux etre a voir pour ajouter les sous race dans un autre bouton apres avoir selectioner la race principal 
        raceComboBox = new JComboBox<>(races);

        // Créer un bouton pour saisir les valeurs
        JButton enterButton = new JButton("Entrer Valeurs");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterValues();
            }
        });

        // Créer un panneau pour les listes déroulantes et le bouton
        JPanel comboPanel = new JPanel();
        comboPanel.add(new JLabel("Profession: "));
        comboPanel.add(professionComboBox);
        comboPanel.add(new JLabel("Race: "));
        comboPanel.add(raceComboBox);
        comboPanel.add(enterButton);
        panel.add(comboPanel, BorderLayout.NORTH);

        // tableaux des caracteritique 
        String[] columnNames = {"Caractéristique", "Valeur", "Valeur Normale", "Race", "Total"};
        Object[][] data = {
                {"Force", 0, 0, "", 0},
                {"Agilité", 0, 0, "", 0},
                {"Constitution", 0, 0, "", 0},
                {"Intelligence", 0, 0, "", 0},
                {"Intuition", 0, 0, "", 0},
                {"Présence", 0, 0, "", 0},
                {"Chance", 0, 0, "", 0},
                {"Apparence", 0, 0, "", 0},
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

    // Saisir les valeurs pour chaque caractéristique et les afficher dans le tableau
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
            }
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
            if (race.equals("Humain") && characteristic.equals("Force")) { //metode pour ajouter auto un 5 en fo pour les humain basique 
                raceValue = 5;
            }
            table.setValueAt(raceValue, i, 3); 

            // Calculer le total
            int total = value + professionValue + raceValue; // Valeur + Valeur Normale (Profession) + Race
            table.setValueAt(total, i, 4);  // affiche le total
        }
    }
}
// code en cours il manque pas mal de chose en soit dont les race a embelire avec les sous race est tout les systeme de competance est leur calcule auto 
// la categorie pour les sort le nom du personnage le calcule des pv les point de chance 