package network.SimpleChat;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    JTextArea area;
    JPanel mainPanel;

    public void go() {
        JFrame frame = new JFrame("Ludicrously Simple Chat Server");
        mainPanel = new JPanel();
        area = new JTextArea("");
        mainPanel.add(area);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(400, 500);
        frame.setVisible(true);
        listen();
    }

    private void listen() {
        try {
            ServerSocket server = new ServerSocket(5000);
            while (true){
                Socket socket = server.accept(); // blocks until connection
                InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                BufferedReader reader = new BufferedReader(stream);
                String message = reader.readLine();
                System.out.println("Got message");
                System.out.println(message);
                addMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMessage(String message) {
        area.append(message);
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        new ChatServer().go();
    }
}
