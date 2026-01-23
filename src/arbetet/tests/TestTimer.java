package tests;

import javax.swing.*;
import java.awt.*;

public class TestTimer {
    public static void main(String[] args) {
        int bredd = 11;
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout(11,11));
        for (int i = 0; i < 121; i++) {
            if( i % bredd == (bredd/2) ){
                JPanel panel = new JPanel();
                panel.setBackground(new Color(0, 0, 0));
                frame.add(panel);
            }
            else {
                JPanel panel = new JPanel();
                panel.setBackground(new Color(77, 187, 122));
                frame.add(panel);

            }
        }

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
