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
                "Håller sig till en väg",
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
            case "Random(men ej bakåt)": körRandom(); break;
            case "Håller sig till en väg": körHV(); break;
            case "See hela labyrinten": körStartOchMål(); break;
            case "Spara och kolla korsningar": körKorsningar(); break;
            case "Räkna rutor till mål": KörGåMotMål(); break;
        }
    }

    // 1. RANDOM (MEN EJ BAKÅT)
    private void körRandom() {
        int x = 0, y = 0, lastX = -1, lastY = -1;
        Random rand = new Random();
        while(!labyrint.ärMål(x,y)){
            ArrayList<Point> möjliga = new ArrayList<>(labyrint.möjligaSteg(x,y));
            ArrayList<Point> utanBakåt = new ArrayList<>(möjliga);
            int fx = lastX, fy = lastY;
            utanBakåt.removeIf(p -> p.x == fx && p.y == fy);
            if(!utanBakåt.isEmpty()) möjliga = utanBakåt;
            if(möjliga.isEmpty()) return;
            Point nästa = möjliga.get(rand.nextInt(möjliga.size()));
            lastX = x; lastY = y;
            x = nästa.x; y = nästa.y;
            labyrint.flyttaTill(x,y);
        }
    }

    // 2. HÅLLER SIG TILL EN VÄG (DFS med Stack)
    private void körHV() {
        Stack<Point> stack = new Stack<>();
        boolean[][] besökt = new boolean[16][16];
        stack.push(new Point(0,0));
        while(!stack.isEmpty()){
            Point p = stack.pop();
            if(besökt[p.x][p.y]) continue;
            besökt[p.x][p.y] = true;
            labyrint.flyttaTill(p.x, p.y);
            if(labyrint.ärMål(p.x, p.y)) return;
            for(Point n : labyrint.möjligaSteg(p.x, p.y)) {
                if(!besökt[n.x][n.y]) stack.push(n);
            }
        }
    }

    // 3. SE HELA LABYRINTEN (BFS med Queue)
    private void körStartOchMål() {
        Queue<Point> q = new LinkedList<>();
        boolean[][] besökt = new boolean[16][16];
        q.add(new Point(0,0));
        while(!q.isEmpty()){
            Point p = q.poll();
            if(besökt[p.x][p.y]) continue;
            besökt[p.x][p.y] = true;
            labyrint.flyttaTill(p.x, p.y);
            if(labyrint.ärMål(p.x, p.y)) return;
            for(Point n : labyrint.möjligaSteg(p.x, p.y)) {
                if(!besökt[n.x][n.y]) q.add(n);
            }
        }
    }

    // 4. SPARA OCH KOLLA KORSNINGAR
    private void körKorsningar() {
        Stack<Point> korsningar = new Stack<>();
        boolean[][] besökt = new boolean[16][16];
        Point current = new Point(0,0);
        while(true){
            if(!besökt[current.x][current.y]) {
                besökt[current.x][current.y] = true;
                labyrint.flyttaTill(current.x, current.y);
            }
            if(labyrint.ärMål(current.x, current.y)) return;

            List<Point> möjliga = labyrint.möjligaSteg(current.x, current.y);
            möjliga.removeIf(p -> besökt[p.x][p.y]);

            if(möjliga.size() > 1) korsningar.push(current);

            if(möjliga.isEmpty()){
                if(korsningar.isEmpty()) return;
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
                    if (d < bästDist) { bäst = p; bästDist = d; }
                }
                historik.push(bäst);
            }
        }
    }

    private int dist(Point p, int mx, int my) {
        return Math.abs(p.x - mx) + Math.abs(p.y - my);
    }
}