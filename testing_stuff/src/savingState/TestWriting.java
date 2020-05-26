package src.savingState;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestWriting {
    public static void main(String[] args) {
        String dataToWrite = "Hello there";
        String dataToWrite2 = "Hello there Buffered";
        String pathFromRoot = "/testing_stuff/src/savingState/data";
        String fileName = "SuperHeroes.txt";
        Path textFilePath = Paths.get(System.getProperty("user.dir"), pathFromRoot, fileName);
        FileWriterClass writer = new FileWriterClass();
        writer.writeToFile(textFilePath, dataToWrite);
        writer.readFromFile(textFilePath);
        writer.writeToFileBuf(textFilePath, dataToWrite2);
        writer.readFromFileBuf(textFilePath);
    }
}

class FileWriterClass {
    public void writeToFile(Path filePath, String data) {
        try {
            FileWriter writer = new FileWriter(filePath.toString());
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromFile(Path filePath) {
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

    public void writeToFileBuf(Path filePath, String data) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString()));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFileBuf(Path filePath) {
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
