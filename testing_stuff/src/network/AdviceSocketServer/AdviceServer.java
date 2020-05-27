package network.AdviceSocketServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AdviceServer {
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            while (true){
                Socket socket = server.accept(); // blocks until connection
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                String advice = getAdvice();
                writer.println(advice);
                writer.close();
                System.out.println("Sent the advice: " + advice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAdvice() {
        String[] adviceList = {"Take smaller bites", "Go for the tight jeans. No they do NOT make you look fat.",
                "One word: inappropriate", "Just for today, be honest. Tell your boss what you *really* think",
                "You might want to rethink that haircut."};

        int random = (int) (Math.random() * adviceList.length);
        return adviceList[random];
    }
}

