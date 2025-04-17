package Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private static final String FILE_NAME = "scores.txt";

    public static void addScore(String time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(time);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getScores() {
        List<String> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scores.sort((s1, s2) -> {
            double time1 = Double.parseDouble(s1);
            double time2 = Double.parseDouble(s2);
            return Double.compare(time1, time2);
        });
        return new ArrayList<>(scores);
    }
}
