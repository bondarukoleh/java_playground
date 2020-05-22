package src.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawPanel {
    public static void main(String[] args) {
        new MyPanel().draw();
    }
}

class MyPanel {
    private JFrame window;
    private JButton changeColorButton;
    private JButton changeLabelButton;
    private JLabel label;
    private GradientCircle circle;
    private ChangeColorButtonListener buttonEventListener;
    private ChangeLabelButtonListener changeLabelButtonListener;

    public MyPanel() {
        window = new JFrame();
        changeColorButton = new JButton("Change Color");
        changeLabelButton = new JButton("Change Label");
        label = new JLabel("Default text");
        circle = new GradientCircle();
        buttonEventListener = new ChangeColorButtonListener();
        changeLabelButtonListener = new ChangeLabelButtonListener();
    }

    public void draw() {
        changeColorButton.addActionListener(buttonEventListener);
        changeLabelButton.addActionListener(changeLabelButtonListener);
        //        window.getContentPane().add(new MyPanel());
//        window.getContentPane().add(new ColoredCircle());
        window.getContentPane().add(BorderLayout.CENTER, circle);
        window.getContentPane().add(BorderLayout.SOUTH, changeColorButton);
        window.getContentPane().add(BorderLayout.EAST, changeLabelButton);
        window.getContentPane().add(BorderLayout.WEST, label);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 600);
        window.setVisible(true);
    }

    class ChangeColorButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            window.repaint();
        }
    }

    class ChangeLabelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            label.setText("Text Changed");
        }
    }
}

class ColoredCircle extends JPanel {
    public void paintComponent(Graphics g) {
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        int red = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        Color randomColor = new Color(red, green, blue);
        g.setColor(randomColor);
        g.fillOval(70, 70, 100, 100);
    }
}

class GradientCircle extends JPanel {
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int red = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        Color startColor = new Color(red, green, blue);
        red = (int) (Math.random() * 255);
        green = (int) (Math.random() * 255);
        blue = (int) (Math.random() * 255);
        Color endColor = new Color(red, green, blue);
        GradientPaint gradient = new GradientPaint(70, 70, startColor, 150, 150, endColor);
        g2d.setPaint(gradient);
        g2d.fillOval(70, 70, 100, 100);
    }
}
