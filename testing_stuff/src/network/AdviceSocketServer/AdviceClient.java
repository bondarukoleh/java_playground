package network.AdviceSocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class AdviceClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(stream);
            String advice = reader.readLine();
            System.out.format("Advice of the day: \"%s\"", advice);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}