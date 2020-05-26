package src.FileSystem;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileClass {
    public static void main(String[] args) {
        TestFile testFile = new TestFile();
        testFile.createDirectory();
    }
}

class TestFile {
    public void createDirectory() {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "/testing_stuff/src/FileSystem");
            File dir = new File(filePath.toString());
            if(dir.isDirectory()){
                String[] folderContent = Objects.requireNonNull(dir.list());
                for (String content : folderContent) {
                    System.out.println(content);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
