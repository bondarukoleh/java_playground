package src.collections;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TestSongsSorting {
    public static void main(String[] args) {
        JukeBox jukeBox = new JukeBox(TestSongsSorting.getSongsFilePath());
//        jukeBox.listByArtist();
//        jukeBox.listBySongs();
        jukeBox.hashSetSongs();
    }

    private static Path getSongsFilePath() {
        String pathFromRoot = "/testing_stuff/src/collections/data";
        String fileName = "songs.txt";
        return Paths.get(System.getProperty("user.dir"), pathFromRoot, fileName);
    }
}

class JukeBox {
    private final ArrayList<Song> songs;
    private final SongsComparator songsSorting = new SongsComparator();

    public JukeBox(Path songsPath){
            this.songs = SongsReader.parseSongList(songsPath);
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void listBySongs() {
        Collections.sort(songs, songsSorting.bySongName());
        System.out.println(songs);
    }

    public void listByArtist() {
        Collections.sort(songs, songsSorting.byArtist());
        System.out.println(songs);
    }

    public void hashSetSongs() {
        HashSet<Song> hashedSongs = new HashSet<>();
        hashedSongs.addAll(songs);
        System.out.println(hashedSongs);
    }

    class SongsComparator {
        public Comparator bySongName(){
            class CompareByName implements Comparator<Song> {
                @Override
                public int compare(Song o1, Song o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            }

            return new CompareByName();
        }

        public Comparator byArtist(){
            class CompareByArtist implements Comparator<Song> {
                @Override
                public int compare(Song o1, Song o2) {
                    return o1.getSinger().compareTo(o2.getSinger());
                }
            }

            return new CompareByArtist();
        }
    }
}

class Song implements Comparable<Song> {
    private final String name;
    private final String singer;
    private String rating;

    public Song(String name, String singer) {
        this.name = name;
        this.singer = singer;
    }

    public Song(String name, String singer, String rating) {
        this.name = name;
        this.singer = singer;
        this.rating = rating;
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

    public int compareTo(Song song) {
        // alphabetic order
        return getName().compareTo(song.getName());
    }
}

class SongsReader {
    public static ArrayList<Song> parseSongList(Path filePath) {
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