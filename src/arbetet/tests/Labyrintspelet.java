package arbetet.tests;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Random;

public class Labyrintspelet {
    private final int rutorPerRad = 16;
    private int längdSida = 640/rutorPerRad;
    private Random random;
    private boolean[][] vertikalaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private boolean[][] horisontellaVäggar = new boolean[rutorPerRad][rutorPerRad];

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        LabyrintPlayField panel = new LabyrintPlayField();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(640,640));
        panel.setBorder(new LineBorder(Color.BLACK,2));

        //boolean[][] array = panel.vertikalVäggEllerEj();
        //panel.horisontellVäggEllerEj();
        frame.add(panel);
        frame.repaint();

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



    }
}
