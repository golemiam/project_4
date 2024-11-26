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

    public static void main(String[] args) throws IOException {
        String inputFile = "board.csv";
        String outputFile = "sorted_data.csv";
        int playerPosition = 0;
        Random random = new Random();
        int diceOne = random.nextInt(5) + 1; // Generates a random integer between 0 and 9 (inclusive)
        int diceTwo = random.nextInt(5) + 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputFile)) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

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
            String str1 = String.valueOf(diceOne);
            System.out.println(str1);
            String str2 = String.valueOf(diceTwo);
            System.out.println(str2);

            for (String l : lines) {
                writer.write(l + "\n");
            }
        }
    }
}

