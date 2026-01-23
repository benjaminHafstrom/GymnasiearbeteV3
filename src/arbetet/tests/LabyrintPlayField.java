package tests;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class LabyrintPlayField extends JPanel {
    private final int rutorPerRad = 16;
    private int längdSida = 640/rutorPerRad;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean[][] vertikalaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private boolean[][] horisontellaVäggar = new boolean[rutorPerRad][rutorPerRad];

    public static void main(String[] args) {
        System.out.println("Height - " + screenSize.height);
        System.out.println("Width - " + screenSize.width);
        JFrame frame = new JFrame();

        frame.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        JPanel panel = new LabyrintPlayField();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(640,640));
        panel.setBorder(new LineBorder(Color.BLACK,2));
        frame.add(panel);
        frame.repaint();

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



    }

    public LabyrintPlayField() {
        horisontellVäggEllerEj();
        vertikalVäggEllerEj();
    }

    public boolean[][] vertikalVäggEllerEj() {
        for (int i = 0; i < rutorPerRad; i++) {
            for (int j = 0; j < rutorPerRad; j++) {
                int a = (int) (Math.random()*2);
                if (a==1) vertikalaVäggar[i][j] = true;
                if (a==0) vertikalaVäggar[i][j] = false;
            }
        }

        return vertikalaVäggar;
    }


    public boolean[][] horisontellVäggEllerEj() {
        for (int i = 0; i < rutorPerRad; i++) {
            for (int j = 0; j < rutorPerRad; j++) {
                int a = (int) (Math.random()*2);
                if (a==1) horisontellaVäggar[i][j] = true;
                if (a==0) horisontellaVäggar[i][j] = false;
            }
        }
        return horisontellaVäggar;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for (int j = 0; j < rutorPerRad; j++) {
            for (int k = 0; k < rutorPerRad; k++) {
                if (horisontellaVäggar[j][k]==true) {
                    g.fillRect( ((j)*längdSida),((k)*längdSida),4,längdSida);
                }
            }
        }
        for (int j = 0; j < rutorPerRad; j++) {
            for (int k = 0; k < rutorPerRad; k++) {
                if (vertikalaVäggar[j][k]==true) {
                    g.fillRect( ((j)*längdSida),((k)*längdSida),längdSida,4);
                }

            }
        }


    }

}

