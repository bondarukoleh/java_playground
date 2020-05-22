package src.player.gui;

import com.sun.security.auth.module.JndiLoginModule;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PlayerGui {
    public static void main(String[] args) {
        PlayerWindow playerWindow = new PlayerWindow();
        playerWindow.show();
    }
}

class PlayerWindow {
    private JFrame window;
    private JButton button;
    private ButtonEventListener buttonEventListener = new ButtonEventListener();
    private MouseEventListener mouseEventListener = new MouseEventListener();

    public PlayerWindow() {
        window = new JFrame();
        button = new JButton("Try to click");
        button.addActionListener(buttonEventListener);
        button.addMouseListener(mouseEventListener);
    }

    public void show() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(button);

        window.setSize(300, 300);
        window.setVisible(true);
    }

    class ButtonEventListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.paramString());
            setButtonText(button,"Clicked");
        }

        public void setButtonText(JButton button, String text){
            button.setText(text);
        }
    }

    class MouseEventListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("mouseClicked event");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("mousePressed event");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("mouseReleased event");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("mouseEntered event");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            System.out.println("mouseExited event");
        }
    }
}




