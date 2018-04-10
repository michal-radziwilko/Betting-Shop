package App;


import GUI.Start;

import javax.swing.*;


public class Aplication {
    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame startFrame = new JFrame ("Zakład bukmacherski");
        Start StartFrame = new Start();
        startFrame.setContentPane(StartFrame.mainPanel);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.pack();
        startFrame.setVisible(true);
        startFrame.setLocationRelativeTo(null);
    }
}
