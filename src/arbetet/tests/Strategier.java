package arbetet.tests;

// combobox med strategierna vi ska testa
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Strategier extends JPanel {

    private JComboBox<String> strategiComboBox;

    public Strategier() {
        setLayout(new FlowLayout());

        // Alternativen i comboboxen
        String[] strategier = {
                "Välj strategi...",
                "Random(men ej bakåt)",
                "Håller sig till en väg",
                "See hela labyrinten",
                "Spara och kolla korsningar",
                "ignorera vägar",
                "Räkna rutor till mål",
        };

        strategiComboBox = new JComboBox<>(strategier);
        add(strategiComboBox);

        // Event som triggas när användaren väljer något
        strategiComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String valt = (String) strategiComboBox.getSelectedItem();
                körProgram(valt);
            }
        });
    }

    private void körProgramm(String strategi) {
        switch (strategi) {
            case "Random(men ej bakåt)":
                körRandom();
                break;
            case "Håller sig till en väg":
                körHV();
                break;
            case "See hela labyrinten":
                körStartOchMål();
                break;
            case "Sparar och kollar korsningar":
                körKorsningar();
                break;
            case "ignorera vägar":
                körIV();
                break;
            case "Räkna rutor till mål":
                KörGåMotMål();
            default:
                break;
        }
    }

    private void körRandom() {
        System.out.println("körRandom(men ej bakåt)");

    }

    private void körHV() {
        System.out.println("körHV");

    }

    private void körStartOchMål() {
        System.out.println("kör See hela labyrinten");
    }

    private void körKorsningar() {
        System.out.println("Kör Sparar och kollar korsningar");
    }

    private void KörGåMotMål()  {
        System.out.println("Kör Räkna rutor till mål");
    }

    private void körRäkna() {
        System.out.println("Kör Räkna");
    }

    private void körIV() {
        System.out.println("Kör IV");
    }
}
