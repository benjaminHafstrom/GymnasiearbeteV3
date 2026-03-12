package arbetet.tests;

import javax.swing.*;

public class Timer {
    private int milliSekund = 0;
    private int sekund = 0;
    private int minut = 0;
    private boolean timerStatus = false;
    private javax.swing.Timer timer;
    private JLabel label;

    public Timer(JLabel label) {
        this.label = label;
        // 10ms ger en mjuk och exakt visning i GUI:et
        this.timer = new javax.swing.Timer(10, e -> UppdateraTid());
    }

    public void start() {
        if (!timerStatus) {
            timer.start();
            timerStatus = true;
        }
    }

    public void stop() {
        timer.stop();
        timerStatus = false;
    }

    public void reset() {
        timer.stop();
        milliSekund = 0;
        sekund = 0;
        minut = 0;
        timerStatus = false;
        uppdateraLabel();
    }

    private void UppdateraTid() {
        milliSekund += 10;
        if (milliSekund >= 1000) {
            milliSekund = 0;
            sekund++;
        }
        if (sekund >= 60) {
            sekund = 0;
            minut++;
        }
        uppdateraLabel();
    }

    private void uppdateraLabel() {
        if (label != null) {
            // %02d betyder att det alltid visas minst två siffror (t.ex. 05 istället för 5)
            label.setText(String.format("Tid: %02d:%02d:%03d", minut, sekund, milliSekund));
        }
    }
}