import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Platform {
    Video head;
    Video tail;
    Platform(){
        head = null;
        tail = null;
    }
    Platform(Video v){
        head = v;
        tail = v;
    }
    static class Video {
        String videoID;
        String videoTitle;
        String channelID;
        String channelTitle;
        String publishedAt;
        int viewCount;
        int likeCount;
        int commentCount;
        int popularity;

        Video next;
        Video(String videoID, String videoTitle, String channelID, String channelTitle, String publishedAt, int viewCount, int likeCount, int commentCount){
            this.videoID = videoID;
            this.videoTitle = videoTitle;
            this.channelID = channelID;
            this.channelTitle = channelTitle;
            this.publishedAt = publishedAt;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
            next = null;
        }

        void play(){
            System.out.println("Reproduciendo video " + this.videoID + ": " + this.videoTitle);
         this.viewCount++;
         System.out.println("Visitas: " + this.viewCount);
        }
        public String getVideoID() {
            return videoID;
        }

        public void setNext(Video next) {
            this.next = next;
        }

        public Video getNext() {
            return next;
        }
    }
    public void calculatePopularity() {
        Video current = head;
        while (current != null) {
            current.popularity = current.viewCount;
            current = current.next;
        }
    }

    Video begin(){
        return head;
    }

    long isNumericLong(String s){
        long d = 0;
        try {
            d = Long.parseLong(s);
        }
        catch (NumberFormatException ignored){}
        return d;
    }
    int isNumericInt(String s){
        int i = 0;
        try {
            i = Integer.parseInt(s);
        }
        catch (NumberFormatException ignored){}
        return i;
    }

    Video arrayToVideo(ArrayList<String> array) {
        String videoID = array.get(0);
        String videoTitle = array.get(1);
        String channelID = array.get(2);
        String channelTitle = array.get(3);
        String publishedAt = array.get(4);
        int viewCount = isNumericInt(array.get(5));
        int likeCount = isNumericInt(array.get(6));
        int commentCount = isNumericInt(array.get(7));
        return new Video(videoID, videoTitle, channelID, channelTitle, publishedAt, viewCount, likeCount, commentCount);
    }
    void insertFromFile(String file){
        String string;
        boolean first = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file));){
            //Skip first
            br.readLine();
            while((string = br.readLine()) != null){
                boolean inQuotes = false;
                int start = 0;
                ArrayList<String> newLines = new ArrayList<>();
                for (int i = 0; i < string.length(); i++) {
                    if (string.charAt(i) == '\"') {
                        inQuotes = !inQuotes;
                    } else if (string.charAt(i) == ',' && !inQuotes) {
                        newLines.add(string.substring(start, i));
                        start = i +1;
                    }
                }
                newLines.add(string.substring(start));
                Video newVideo = arrayToVideo(newLines);
                insertAtEnd(newVideo);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    

    Video recursiveLast(Video v){
        if (v == null) {
            return null; // o lanzar una excepción, dependiendo del caso de uso
        }
        if (v.next != null){
            return recursiveLast(v.next);
        }
        else{
            return v;
        }
    }
    Video iterativeLast(Video v){
        while(v.next != null){
            v = v.next;
        }
        return v;
    }
    void insertAtEnd(Video v) {
        if (head == null) {
            head = v;
            tail = v;
        } else {
            tail.next = v;
            tail = v;
        }
    }
    
    void recursivePrint(Video v){
        if(v == null){
            return;
        }
        System.out.println(v);
        recursivePrint(v.next);
    }

    public void iterativePrint() {
        Video current = head;
        while (current != null) {
            System.out.print(current.toString() + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    public Video search(String videoID) {
        Video current = head;
        while (current != null) {
            if (current.getVideoID().equals(videoID)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }
    void reverse(Video v) {
        Video prev = null;
        Video curr = v;
        Video next = null;
    
        while (curr != null) {
            next = curr.getNext();
            curr.setNext(prev);
            prev = curr;
            curr = next;
        }
         v = prev;
    }
    
    public static void main(String[] args) {
        

        //pruebas para la API
        Platform platform = new Platform();
        String file = "YoutubeDTSV2.csv";
        platform.insertFromFile(file);
        platform.reverse(platform.begin());
        platform.iterativePrint();
        System.out.println("search: " + platform.search("y83x7MgzWOA"));
        platform.calculatePopularity();
        ArrayList<Video> searchResults = platform.search("cat videos");
        for (Video video : searchResults) {
            System.out.println(video.videoTitle);
        }

        // Reproducir primer video de la lista
        Video actualVideo = platform.begin();
        actualVideo.play();
    } 
    }




