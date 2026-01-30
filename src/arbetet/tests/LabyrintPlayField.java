package arbetet.tests;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


public class LabyrintPlayField extends JPanel {
    private final int rutorPerRad = 16;
    private final int storlekLabyrint = 640;
    private int längdSida = storlekLabyrint/rutorPerRad;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean[][] vertikalaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private boolean[][] horisontellaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private int vertikalMålPosition = (((int) ((Math.random()*rutorPerRad/2)+(rutorPerRad/2)))*längdSida);
    private int horisontellMålPosition = (((int) ((Math.random()*rutorPerRad/2)+(rutorPerRad/2)))*längdSida);
    private int nuvarandeXPosition = 10;
    private int nuvarandeYPosition = 10;

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
    public boolean[][] generaVägTillMål(){
        if (nuvarandeXPosition == 10) {
            if (nuvarandeYPosition == 10) {
                int nerEllerHöger = (int) (Math.random() * 2);
                if (nerEllerHöger == 0) nuvarandeYPosition += längdSida;//ner
                if (nerEllerHöger == 1) nuvarandeXPosition += längdSida;//höger
            }
            else {
                int uppNerEllerHöger = (int) (Math.random() * 3);
                if (uppNerEllerHöger == 0) nuvarandeYPosition -= längdSida;//upp
                if (uppNerEllerHöger == 1) nuvarandeYPosition += längdSida;//ner
                if (uppNerEllerHöger == 2) nuvarandeXPosition += längdSida;//höger
            }
        }
        if (nuvarandeXPosition == storlekLabyrint-10){
            if (nuvarandeYPosition == 10){
                int nerEllerVänster = (int)(Math.random()*2);
                if (nerEllerVänster ==0) nuvarandeYPosition+=längdSida;//ner
                if (nerEllerVänster ==1) nuvarandeXPosition-=längdSida;//vänster
            }
            else {
                int uppNerEllerVänster = (int) (Math.random() * 3);
                if (uppNerEllerVänster == 0) nuvarandeYPosition -= längdSida;//upp
                if (uppNerEllerVänster == 1) nuvarandeYPosition += längdSida;//ner
                if (uppNerEllerVänster == 2) nuvarandeXPosition -= längdSida;//vänster
            }
        }
        if (nuvarandeXPosition == 10) {
            if (nuvarandeYPosition == storlekLabyrint - 10) {
                int uppEllerHöger = (int) (Math.random() * 2);
                if (uppEllerHöger == 0) nuvarandeYPosition -= längdSida;//upp
                if (uppEllerHöger == 1) nuvarandeXPosition += längdSida;//höger
            }
            else {
                int uppHögerEllerVänster = (int) (Math.random() * 3);
                if (uppHögerEllerVänster == 0) nuvarandeYPosition -= längdSida;//upp
                if (uppHögerEllerVänster == 1) nuvarandeXPosition += längdSida;//höger
                if (uppHögerEllerVänster == 2) nuvarandeXPosition -= längdSida;//vänster
            }
        }
        if (nuvarandeXPosition == storlekLabyrint-10) {
            if (nuvarandeYPosition == storlekLabyrint - 10) {
                int uppEllerVänster = (int) (Math.random() * 2);
                if (uppEllerVänster == 0) nuvarandeYPosition -= längdSida;//upp
                if (uppEllerVänster == 1) nuvarandeXPosition -= längdSida;//vänster
            }
            else {
                int nerHögerEllerVänster = (int) (Math.random() * 3);
                if (nerHögerEllerVänster == 0) nuvarandeYPosition += längdSida;//ner
                if (nerHögerEllerVänster == 1) nuvarandeXPosition += längdSida;//höger
                if (nerHögerEllerVänster == 2) nuvarandeXPosition -= längdSida;//vänster
            }
        }
        else {
            int allaRiktningar = (int) (Math.random() * 4);
            if (allaRiktningar == 0) nuvarandeXPosition += längdSida;//höger
            if (allaRiktningar == 1) nuvarandeXPosition -= längdSida;//vänster
            if (allaRiktningar == 2) nuvarandeYPosition += längdSida;//ner
            if (allaRiktningar == 3) nuvarandeYPosition -= längdSida;//upp

        }
                return vertikalaVäggar;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.green);
        g.fillRect(0,0,längdSida,längdSida);
        g.setColor(Color.yellow);
        g.fillRect(vertikalMålPosition,horisontellMålPosition,längdSida,längdSida);
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

