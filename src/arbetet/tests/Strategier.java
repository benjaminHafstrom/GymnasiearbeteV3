package arbetet.tests;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Strategier extends JPanel {

    private JComboBox<String> strategiComboBox;
    private LabyrintPlayField labyrint;
    private JLabel timerLabel;
    private Timer algoritmTimer;
    private boolean isRunning = false;

    public Strategier(LabyrintPlayField labyrint) {
        this.labyrint = labyrint;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        timerLabel = new JLabel("Tid: 00:00:000");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setForeground(Color.RED);
        add(timerLabel);

        algoritmTimer = new Timer(timerLabel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        String[] strategier = {
                "Välj strategi...",
                "Random(men ej bakåt)",
                "Håller sig till en vägg",
                "See hela labyrinten",
                "Spara och kolla korsningar",
                "Räkna rutor till mål",
        };

        strategiComboBox = new JComboBox<>(strategier);
        strategiComboBox.setMaximumSize(new Dimension(200, 30));
        strategiComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(strategiComboBox);

        strategiComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRunning) return;

                String valt = (String) strategiComboBox.getSelectedItem();
                if (valt.equals("Välj strategi...")) return;

                new Thread(() -> {
                    isRunning = true;
                    algoritmTimer.reset();
                    labyrint.resetPath();
                    algoritmTimer.start();

                    körProgram(valt);

                    algoritmTimer.stop();
                    isRunning = false;
                }).start();
            }
        });
    }

    private void körProgram(String strategi) {
        switch (strategi) {
            case "Random(men ej bakåt)":
                körRandom();
                break;
            case "Håller sig till en vägg":
                körHV();
                break;
            case "See hela labyrinten":
                körStartOchMål();
                break;
            case "Spara och kolla korsningar":
                körKorsningar();
                break;
            case "Räkna rutor till mål":
                KörGåMotMål();
                break;
        }
    }

    // 1. RANDOM (MEN EJ BAKÅT)
    private void körRandom() {
        int x = 0, y = 0, lastX = -1, lastY = -1;
        Random rand = new Random();
        while (!labyrint.ärMål(x, y)) {
            ArrayList<Point> möjliga = new ArrayList<>(labyrint.möjligaSteg(x, y));
            ArrayList<Point> utanBakåt = new ArrayList<>(möjliga);
            int fx = lastX, fy = lastY;
            utanBakåt.removeIf(p -> p.x == fx && p.y == fy);
            if (!utanBakåt.isEmpty()) möjliga = utanBakåt;
            if (möjliga.isEmpty()) return;
            Point nästa = möjliga.get(rand.nextInt(möjliga.size()));
            lastX = x;
            lastY = y;
            x = nästa.x;
            y = nästa.y;
            labyrint.flyttaTill(x, y);
        }
    }

    // 2. HÅLLER SIG TILL EN VÄGG (högerhandsregeln)
    private void körHV() {
        int x = 0, y = 0;

        // 0 = upp, 1 = höger, 2 = ner, 3 = vänster
        int riktning = 1; // startar åt höger

        labyrint.flyttaTill(x, y);

        while (!labyrint.ärMål(x, y)) {

            // testa höger
            int höger = (riktning + 1) % 4;

            if (kanGå(x, y, höger)) {
                riktning = höger;
            }
            // annars rakt fram
            else if (kanGå(x, y, riktning)) {
                // behåll riktning
            }
            // annars vänster
            else if (kanGå(x, y, (riktning + 3) % 4)) {
                riktning = (riktning + 3) % 4;
            }
            // annars vänd om
            else {
                riktning = (riktning + 2) % 4;
            }

            int[] nästa = steg(x, y, riktning);
            x = nästa[0];
            y = nästa[1];

            labyrint.flyttaTill(x, y);
        }
    }

    // 3. SE HELA LABYRINTEN (BFS med Queue)
    private void körStartOchMål() {
        Queue<Point> q = new LinkedList<>();
        boolean[][] besökt = new boolean[16][16];

        Point current = new Point(0, 0);
        q.add(current);

        labyrint.flyttaTill(current.x, current.y);

        while (!q.isEmpty()) {
            Point target = q.poll();

            // 🔥 Gå dit via riktig väg från NUVARANDE position
            List<Point> väg = hittaVäg(current, target);

            for (Point steg : väg) {
                labyrint.flyttaTill(steg.x, steg.y);
            }

            current = target;

            if (besökt[current.x][current.y]) continue;
            besökt[current.x][current.y] = true;

            if (labyrint.ärMål(current.x, current.y)) return;

            for (Point n : labyrint.möjligaSteg(current.x, current.y)) {
                if (!besökt[n.x][n.y]) {
                    q.add(n);
                }
            }
        }
    }

    // 4. SPARA OCH KOLLA KORSNINGAR
    private void körKorsningar() {
        Stack<Point> korsningar = new Stack<>();
        boolean[][] besökt = new boolean[16][16];
        Point current = new Point(0, 0);
        while (true) {
            if (!besökt[current.x][current.y]) {
                besökt[current.x][current.y] = true;
                labyrint.flyttaTill(current.x, current.y);
            }
            if (labyrint.ärMål(current.x, current.y)) return;

            List<Point> möjliga = labyrint.möjligaSteg(current.x, current.y);
            möjliga.removeIf(p -> besökt[p.x][p.y]);

            if (möjliga.size() > 1) korsningar.push(current);

            if (möjliga.isEmpty()) {
                if (korsningar.isEmpty()) return;
                current = korsningar.pop();
                // Vi flyttar "visuellt" tillbaka till korsningen
                labyrint.flyttaTill(current.x, current.y);
            } else {
                current = möjliga.get(0);
            }
        }
    }

    // 5. RÄKNA RUTOR TILL MÅL (Heuristik + Backtracking)
    private void KörGåMotMål() {
        boolean[][] besökt = new boolean[16][16];
        Stack<Point> historik = new Stack<>();
        int målX = labyrint.getMålX();
        int målY = labyrint.getMålY();

        historik.push(new Point(0, 0));

        while (!historik.isEmpty()) {
            Point nu = historik.peek();
            if (!besökt[nu.x][nu.y]) {
                besökt[nu.x][nu.y] = true;
                labyrint.flyttaTill(nu.x, nu.y);
            }

            if (labyrint.ärMål(nu.x, nu.y)) return;

            List<Point> möjliga = labyrint.möjligaSteg(nu.x, nu.y);
            möjliga.removeIf(p -> besökt[p.x][p.y]);

            if (möjliga.isEmpty()) {
                historik.pop();
                if (!historik.isEmpty()) {
                    labyrint.flyttaTill(historik.peek().x, historik.peek().y);
                }
            } else {
                Point bäst = möjliga.get(0);
                int bästDist = dist(bäst, målX, målY);
                for (Point p : möjliga) {
                    int d = dist(p, målX, målY);
                    if (d < bästDist) {
                        bäst = p;
                        bästDist = d;
                    }
                }
                historik.push(bäst);
            }
        }
    }

    private int dist(Point p, int mx, int my) {
        return Math.abs(p.x - mx) + Math.abs(p.y - my);
    }


    private boolean kanGå(int x, int y, int riktning) {
        int[] n = steg(x, y, riktning);
        List<Point> möjliga = labyrint.möjligaSteg(x, y);

        for (Point p : möjliga) {
            if (p.x == n[0] && p.y == n[1]) return true;
        }
        return false;
    }

    private int[] steg(int x, int y, int riktning) {
        switch (riktning) {
            case 0:
                return new int[]{x, y - 1}; // upp
            case 1:
                return new int[]{x + 1, y}; // höger
            case 2:
                return new int[]{x, y + 1}; // ner
            case 3:
                return new int[]{x - 1, y}; // vänster
        }
        return new int[]{x, y};
    }

    private List<Point> hittaVäg(Point start, Point mål) {
        Queue<Point> q = new LinkedList<>();
        Map<Point, Point> parent = new HashMap<>();
        boolean[][] besökt = new boolean[16][16];

        q.add(start);
        parent.put(start, null);

        while (!q.isEmpty()) {
            Point p = q.poll();

            if (besökt[p.x][p.y]) continue;
            besökt[p.x][p.y] = true;

            if (p.equals(mål)) break;

            for (Point n : labyrint.möjligaSteg(p.x, p.y)) {
                if (!besökt[n.x][n.y] && !parent.containsKey(n)) {
                    parent.put(n, p);
                    q.add(n);
                }
            }
        }

        // bygg väg baklänges
        List<Point> path = new ArrayList<>();
        Point temp = mål;

        while (temp != null && !temp.equals(start)) {
            path.add(temp);
            temp = parent.get(temp);
        }

        Collections.reverse(path);
        return path;
    }
}