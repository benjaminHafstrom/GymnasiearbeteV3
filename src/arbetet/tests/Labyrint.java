package arbetet.tests;

import javax.swing.*;
import java.awt.*;

public class Labyrint {

    private Labyrint() {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        JLabel header = new JLabel("Labyrint 16x16", SwingConstants.CENTER);
        header.setFont(new Font("monospaced", Font.PLAIN, 40));
        header.setForeground(Color.blue);
        frame.add(header, BorderLayout.NORTH);

        LabyrintPlayField labyrinten = new LabyrintPlayField();
        labyrinten.setPreferredSize(new Dimension(640, 640));
        frame.add(labyrinten, BorderLayout.CENTER);

        Strategier strategipanel = new Strategier(labyrinten);
        strategipanel.setPreferredSize(new Dimension(200, 640));
        frame.add(strategipanel, BorderLayout.EAST);

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
