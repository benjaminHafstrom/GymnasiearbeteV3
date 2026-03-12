package arbetet.tests;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class LabyrintPlayField extends JPanel {
    private final int rutorPerRad = 16;
    private final int storlekLabyrint = 640;
    private int längdSida = storlekLabyrint / rutorPerRad;
    private boolean[][] vertikalaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private boolean[][] horisontellaVäggar = new boolean[rutorPerRad][rutorPerRad];
    private LinkedList<Point> pointlista = new LinkedList<>();
    private int playerX = 0;
    private int playerY = 0;
    private List<Point> playerPath = new ArrayList<>();
    private int[][] besöksOrdning = new int[rutorPerRad][rutorPerRad];

    public LabyrintPlayField() {
        skapaPointLista();
        horisontellVäggEllerEj();
        vertikalVäggEllerEj();
        konverteraPointListanTillDubbelarrayer();
    }

    // Hjälpmetoder för mål
    public boolean ärMål(int x, int y) {
        Point mål = pointlista.get(pointlista.size() - 1);
        return mål.x == x && mål.y == y;
    }
    public int getMålX() { return pointlista.get(pointlista.size() - 1).x; }
    public int getMålY() { return pointlista.get(pointlista.size() - 1).y; }

    public List<Point> möjligaSteg(int x, int y) {
        List<Point> list = new ArrayList<>();
        if (!vertikalaVäggar[x][y] && x > 0) list.add(new Point(x - 1, y));
        if (x < 15 && !vertikalaVäggar[x + 1][y]) list.add(new Point(x + 1, y));
        if (!horisontellaVäggar[x][y] && y > 0) list.add(new Point(x, y - 1));
        if (y < 15 && !horisontellaVäggar[x][y + 1]) list.add(new Point(x, y + 1));
        return list;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Bakgrund & Mål
        g.setColor(Color.green);
        g.fillRect(0, 0, längdSida, längdSida);
        g.setColor(Color.yellow);
        g.fillRect(getMålX() * längdSida, getMålY() * längdSida, längdSida, längdSida);

        // Väggar
        g.setColor(Color.BLACK);
        for (int j = 0; j < rutorPerRad; j++) {
            for (int k = 0; k < rutorPerRad; k++) {
                if (horisontellaVäggar[j][k]) g.fillRect(j * längdSida, k * längdSida, längdSida, 4);
                if (vertikalaVäggar[j][k]) g.fillRect(j * längdSida, k * längdSida, 4, längdSida);
            }
        }

        // Siffror
        g.setColor(Color.BLUE);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        for (int x = 0; x < rutorPerRad; x++) {
            for (int y = 0; y < rutorPerRad; y++) {
                if (besöksOrdning[x][y] > 0) {
                    String s = String.valueOf(besöksOrdning[x][y]);
                    int tx = x * längdSida + (längdSida - fm.stringWidth(s)) / 2;
                    int ty = y * längdSida + (längdSida - fm.getHeight()) / 2 + fm.getAscent();
                    g.drawString(s, tx, ty);
                }
            }
        }

        // Spelare
        g.setColor(Color.RED);
        g.fillOval(playerX * längdSida + längdSida / 4, playerY * längdSida + längdSida / 4, längdSida / 2, längdSida / 2);
    }

    public void resetPath() {
        playerPath.clear();
        besöksOrdning = new int[rutorPerRad][rutorPerRad];
        repaint();
    }

    public void flyttaTill(int x, int y) {
        playerX = x;
        playerY = y;
        playerPath.add(new Point(x, y));
        besöksOrdning[x][y] = playerPath.size();

        // Uppdatera GUI trådsäkert
        SwingUtilities.invokeLater(() -> repaint());

        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    // --- (Behåll skapaPointLista, konverteraPointListanTillDubbelarrayer, osv. från tidigare) ---
    public LinkedList skapaPointLista(){
        pointlista.add(new Point(0,0));
        Point nu = pointlista.get(0);
        Point förra = null;
        int tillbakaKaka = -1;
        while (nu.y< rutorPerRad -5 || nu.x< rutorPerRad -5) {
            int x = nu.x; int y = nu.y;
            int vilketHåll = (int) (Math.random()*30);
            Point p;
            if (vilketHåll<9){
                if (y== rutorPerRad-1 || tillbakaKaka==2) continue;
                p = new Point(nu.x, nu.y+1); tillbakaKaka = 0;
            } else if (vilketHåll<14){
                if (y==0 || tillbakaKaka==3) continue;
                p = new Point(nu.x, nu.y-1); tillbakaKaka = 1;
            } else if (vilketHåll<24){
                if (x== rutorPerRad -1 || tillbakaKaka==0) continue;
                p = new Point(nu.x+1, nu.y); tillbakaKaka = 2;
            } else {
                if (x==0 || tillbakaKaka==1) continue;
                p = new Point(nu.x-1, nu.y); tillbakaKaka = 3;
            }
            if (p.equals(förra)) continue;
            pointlista.add(p); förra = nu; nu = p;
        }
        return pointlista;
    }

    public void konverteraPointListanTillDubbelarrayer(){
        for (int i = 0; i < pointlista.size() - 1; i++) {
            Point a = pointlista.get(i), b = pointlista.get(i + 1);
            int dx = b.x - a.x, dy = b.y - a.y;
            if (dx == 1) vertikalaVäggar[b.x][b.y] = false;
            else if (dx == -1) vertikalaVäggar[a.x][a.y] = false;
            else if (dy == 1) horisontellaVäggar[b.x][b.y] = false;
            else if (dy == -1) horisontellaVäggar[a.x][a.y] = false;
        }
    }

    public boolean[][] vertikalVäggEllerEj() {
        for (int i = 0; i < rutorPerRad; i++) for (int j = 0; j < rutorPerRad; j++) {
            int a = (int) (Math.random()*5); vertikalaVäggar[i][j] = (a>=1);
        }
        return vertikalaVäggar;
    }

    public boolean[][] horisontellVäggEllerEj() {
        for (int i = 0; i < rutorPerRad; i++) for (int j = 0; j < rutorPerRad; j++) {
            int a = (int) (Math.random()*5); horisontellaVäggar[i][j] = (a>=1);
        }
        return horisontellaVäggar;
    }
}