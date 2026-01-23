package arbetet.tests;

import javax.swing.*;
import java.awt.*;

public class Timer {
    private  int milliSekund = 0;
    private int sekund = 0;
    private int minut = 0;
    private boolean timerStatus = false;
    private javax.swing.Timer timer;



    public Timer() { timer = new javax.swing.Timer(1, e-> UppdateraTid()); }

    public void start(){
        if (!timerStatus) {
            timerStatus = true;
            timer.start();
        }
    }
    public void stop(){
        if (timerStatus) {
            timerStatus = false;
            timer.stop();
        }
    }
    public void reset(){
        if (timerStatus) {
            timer.stop();
            milliSekund = 0;
            sekund = 0;
            minut = 0;

        }
    }
    private void UppdateraTid (){
        milliSekund++;
        if (milliSekund >= 1000) {
            milliSekund = 0;
            sekund++;
        }
        if (sekund >= 60) {
            sekund =  0;
            minut++;
        }
    }

    @Override
    public String toString() {
        return String.format("%1$d, %2$d, %3$d", minut, sekund, milliSekund);
    }

    public static void main(String[] args) {
        Timer a = new Timer();
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        FlowLayout flowlayout = new FlowLayout();
        frame.setLayout(flowlayout);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
