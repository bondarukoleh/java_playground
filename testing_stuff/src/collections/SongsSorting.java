package collections;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SongsSorting {
    public static void main(String[] args) {
        Path songsPath = SongsSorting.getSongsFilePath();
        ArrayList<Song> songsReader = SongsReader.readFrom(songsPath);
        JukeBox jukeBox = new JukeBox(songsReader);
        jukeBox.listSongs();
    }

    private static Path getSongsFilePath() {
        String pathFromRoot = "/testing_stuff/src/collections/data";
        String fileName = "songs.txt";
        return Paths.get(System.getProperty("user.dir"), pathFromRoot, fileName);
    }
}

class JukeBox {
    private final ArrayList<Song> songs;

    public JukeBox(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void listSongs() {
        Collections.sort(songs);
        System.out.println(songs);
    }
}

class Song {
    private final String name;
    private final String singer;
    private String rating;
    private String bnp;

    public Song(String name, String singer) {
        this.name = name;
        this.singer = singer;
    }

    public Song(String name, String singer, String rating, String bnp) {
        this.name = name;
        this.singer = singer;
        this.rating = rating;
        this.bnp = bnp;
    }


    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    @Override
    public String toString() {
        return String.format("\n Name: '%s', Singer: '%s'", getName(), getSinger());
    }
}

class SongsReader {
    public static ArrayList<Song> readFrom(Path filePath) {
        ArrayList<Song> songs = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
            String line;
            while ((line = reader.readLine()) != null) {
                songs.add(parseSong(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs;
    }

    private static Song parseSong(String line) {
        String[] song = line.split("/");
        if (song.length != 2) {
            throw new Error("Not valid song cannot be parsed: \"" + line + "\".");
        }
        return new Song(song[0], song[1]);
    }
}