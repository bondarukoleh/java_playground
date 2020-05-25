package savingState;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestWriting {
    public static void main(String[] args) {
        String dataToWrite = "Hello there";
        String dataToWrite2 = "Hello there Buffered";
        FileWriterClass writer = new FileWriterClass();
        writer.writeToFile(dataToWrite);
        writer.readFromFile();
        writer.writeToFileBuf(dataToWrite2);
        writer.readFromFileBuf();
    }
}

class FileWriterClass {
    public void writeToFile(String data) {
        Path filePath = Paths.get(System.getProperty("user.dir"), "data", "SuperHeroes.txt");
        try {
            FileWriter writer = new FileWriter(filePath.toString());
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromFile() {
        Path filePath = Paths.get(System.getProperty("user.dir"), "data", "SuperHeroes.txt");
        try {
            FileReader reader = new FileReader(filePath.toString());
            int i;
            while ((i = reader.read()) != -1){
                System.out.print((char) i);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFileBuf(String data) {
        Path filePath = Paths.get(System.getProperty("user.dir"), "data", "SuperHeroes.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFileBuf() {
        Path filePath = Paths.get(System.getProperty("user.dir"), "data", "SuperHeroes.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
            String line;
            while ((line = reader.readLine()) != null){
                System.out.print(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
