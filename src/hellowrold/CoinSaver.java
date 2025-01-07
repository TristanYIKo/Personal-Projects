package hellowrold;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class CoinSaver {
    private static final String FILE_PATH = "src/hellowrold/Assets/data/coin_count.txt "; // Path to the file"
    public int totalNumberOfCoins;
    
    
    public static void saveCoinCount(int numberOfCoins) {
        int totalCoins = CoinSaver.loadCoinCount();
        totalCoins = totalCoins + numberOfCoins;
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println(totalCoins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int loadCoinCount() {
        int countCoins = 0;
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            if (scanner.hasNextInt()) {
                countCoins = scanner.nextInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countCoins;
    }
}