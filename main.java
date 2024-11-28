import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class Main {
    MonoCards chanceAndChest = new MonoCards();

    public static void main(String[] args) throws IOException {
        String inputFile = "board.csv";
        String outputFile = "sorted_data.csv";
        int playerPosition = 0;
        Random random = new Random();


        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputFile)) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            int amountOfRolls = 10;
            for (int i = 1; i <= amountOfRolls; i++) {
                String str1 = String.valueOf(playerPosition);
                System.out.println(str1);
                int diceOne = random.nextInt(5) + 1; // Generates a random integer between 0 and 9 (inclusive)
                int diceTwo = random.nextInt(5) + 1;
                int totalRolled = diceTwo + diceOne;
                if (playerPosition == 8 || playerPosition == 23 ||playerPosition == 37) {
                    System.out.println("Check");

                    int positionChance = MonoCards.drawChance(playerPosition);
                    playerPosition = positionChance;
                }
                if (playerPosition == 3 || playerPosition == 18 ||playerPosition == 34) {
                    System.out.println("Check");

                    int positionComm = MonoCards.drawComm(playerPosition);
                    playerPosition = positionComm;
                }
                if (playerPosition <= 40) {
                    playerPosition += totalRolled;
                }
                if (playerPosition > 40) {
                    playerPosition -= 40;
                }
                int rolledTimes = 0;
                if (diceOne == diceTwo) {

                    if (rolledTimes < 3) {
                        rolledTimes += 1;
                        diceOne = random.nextInt(5) + 1; // Generates a random integer between 0 and 9 (inclusive)
                        diceTwo = random.nextInt(5) + 1;
                        totalRolled = diceTwo + diceOne;
                        if (playerPosition <= 40) {
                            playerPosition += totalRolled;
                        } else if (playerPosition > 40) {
                            playerPosition -= 40;
                        }



                    } else if (rolledTimes >= 3) {
                        playerPosition = 31;
                        rolledTimes = 0;


                    }

                } else if (diceOne != diceTwo) {
                    rolledTimes = 0;
                }
            };
            // Sort by the first column (splitting by comma)
            //Collections.sort(lines, Comparator.comparing(l -> l.split(",")[0]));

            int columnIndex = 1; // Index of the column you want to extract (starting from 0)

            // Using CSVReader to print values from the specified column
            try (CSVReader csvReader = new CSVReader(new FileReader(inputFile))) {
                List<String[]> records = csvReader.readAll();

                for (String[] record : records) {
                    if (record.length > columnIndex) {
                        System.out.println(record[columnIndex]);
                    }
                }
            } catch (CsvException e) {
                e.printStackTrace();
            }
            String str1 = String.valueOf(playerPosition);
            System.out.println(str1);


            for (String l : lines) {
                writer.write(l + "\n");
            }
        }
    }
}
