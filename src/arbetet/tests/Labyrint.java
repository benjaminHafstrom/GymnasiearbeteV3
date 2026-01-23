package arbetet.tests;

import javax.swing.*;
import java.awt.*;

public class Labyrint {

    private Labyrint() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JLabel header = new JLabel("Labyrint 16x16");
        header.setFont(new Font("monospaced", Font.PLAIN,75));
        header.setForeground(Color.blue);
        frame.add(header, BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        frame.add(new Strategier());


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JPanel GetTimer() {
        JPanel panel = new JPanel();
        Font font = new Font(null,Font.PLAIN, 40);
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JLabel timer = new JLabel("Time: ");
        timer.setFont(font);
        panel.add(timer);


        return panel;
    }

    public static void main(String[] args) {
        new Labyrint();
    }
}
