package hellowrold;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HighScoreManager {
    private static final String FILE_PATH = "src/hellowrold/Assets/data/high_score.txt"; // Path to the file"

    public static void saveHighScore(int score) {
        int highScore = loadHighScore();
        if (score > highScore) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
                writer.println(score);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int loadHighScore() {
        int highScore = 0;
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            if (scanner.hasNextInt()) {
                highScore = scanner.nextInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScore;
    }
}