package arbetet.tests;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedList;


public class LabyrintPlayField extends JPanel {
    private final int rutorPerRad = 16;
    private final int storlekLabyrint = 640;
    private int längdSida = storlekLabyrint/rutorPerRad;
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean[][] vertikalaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private boolean[][] horisontellaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private LinkedList<Point> pointlista = new LinkedList<>();



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
    public LinkedList skapaPointLista(){
        pointlista.add(new Point(0,0));
        Point nu = pointlista.get(0);
        Point förra = null;
        int tillbakaKaka = -1;
        while (nu.y< rutorPerRad -1) {
            int x = nu.x;
            int y = nu.y;
            int vilketHåll = (int) (Math.random()*30);
            Point p;
            if (vilketHåll<9){
                if (tillbakaKaka==2) continue;
                p = new Point(nu.x, nu.y+1);
                tillbakaKaka = 0;
            }
            else if (vilketHåll<14 && vilketHåll>=9 ){
                if (y==0) continue;
                if (tillbakaKaka==3) continue;
                p = new Point(nu.x, nu.y-1);
                tillbakaKaka = 1;
            }
            else if (vilketHåll<24 && vilketHåll>=14){
                if (x== rutorPerRad -1) continue;
                if (tillbakaKaka==0) continue;
                p = new Point(nu.x+1, nu.y);
                tillbakaKaka = 2;
            }
            else{
                if (x==0) continue;
                if (tillbakaKaka==1) continue;
                p = new Point(nu.x-1, nu.y);
                tillbakaKaka = 3;
            }
            if (p.equals(förra)) continue;
            pointlista.add(p);
            förra = nu;
            nu = p;
        }
        System.out.println("rutor till slut: " + pointlista.size());
        return pointlista;
    }

    public void konverteraPointListanTillDubbelarrayer(){
        for (int i = 0; i < pointlista.size() - 1; i++) {

            Point a = pointlista.get(i);
            Point b = pointlista.get(i + 1);

            int skillnadX = b.x - a.x;
            int skillnadY = b.y - a.y;

            if (skillnadX == 1) { // höger
                vertikalaVäggar[a.y][a.x] = false;
            }
            else if (skillnadX == -1) { // vänster
                vertikalaVäggar[b.y][b.x] = false;
            }
            else if (skillnadY == 1) { // ner
                horisontellaVäggar[a.y][a.x] = false;
            }
            else if (skillnadY == -1) { // upp
                horisontellaVäggar[b.y][b.x] = false;
            }
        }
    }

    public LabyrintPlayField() {
        skapaPointLista();
        horisontellVäggEllerEj();
        vertikalVäggEllerEj();
        konverteraPointListanTillDubbelarrayer();
    }

    public boolean[][] vertikalVäggEllerEj() {
        for (int i = 0; i < rutorPerRad; i++) {
            for (int j = 0; j < rutorPerRad; j++) {
                int a = (int) (Math.random()*5);
                if (a>=1) vertikalaVäggar[i][j] = true;
                if (a<=1 && a>=0) vertikalaVäggar[i][j] = false;
            }
        }

        return vertikalaVäggar;
    }


    public boolean[][] horisontellVäggEllerEj() {
        for (int i = 0; i < rutorPerRad; i++) {
            for (int j = 0; j < rutorPerRad; j++) {
                int a = (int) (Math.random()*5);
                if (a>=1) horisontellaVäggar[i][j] = true;
                if (a<=1 && a>=0) horisontellaVäggar[i][j] = false;
            }
        }
        return horisontellaVäggar;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.green);
        g.fillRect(0,0,längdSida,längdSida);
        g.setColor(Color.yellow);
        g.fillRect(pointlista.get(pointlista.size()-2).x*längdSida,pointlista.get(pointlista.size()-2).y*längdSida,längdSida,längdSida);
        g.setColor(Color.BLACK);
        for (int j = 0; j < rutorPerRad; j++) {
            for (int k = 0; k < rutorPerRad; k++) {
                if (horisontellaVäggar[k][j]==true) {
                    g.fillRect( ((k)*längdSida),((j)*längdSida),längdSida,4);
                }
            }
        }
        for (int j = 0; j < rutorPerRad; j++) {
            for (int k = 0; k < rutorPerRad; k++) {
                if (vertikalaVäggar[k][j]==true) {
                    g.fillRect( ((k)*längdSida),((j)*längdSida),4,längdSida);
                }

            }
        }


    }

}

