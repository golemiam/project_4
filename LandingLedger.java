import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Valor Goff
 */
public class LandingLedger {
    private static final String HEADER_1 = ",\"n 1,000\",,\"n 10,000\",,\"n 100,000\",,\"n 1,000,000\",";
    private static final String HEADER_2 = ",count,%,count,%,count,%,count,%";
    private static final String[] SPACES = { "Go", "Mediterranean Avenue", "Community Chest", "Baltic Avenue",
            "Income Tax", "Reading Railroad", "Oriental Avenue", "Chance", "Vermont Avenue", "Connecticut Avenue",
            "Jail", "St. Charles Place", "Electric Company", "States Avenue", "Virginia Avenue",
            "Pennsylvania Railroad", "St. James Place", "Community Chest", "Tennessee Avenue", "New York Avenue",
            "Free Parking", "Kentucky Avenue", "Chance", "Indiana Avenue", "Illinois Avenue", "B. & O. Railroad",
            "Atlantic Avenue", "Ventnor Avenue", "Water Works", "Marvin Gardens", "Go To Jail", "Pacific Avenue",
            "North Carolina Avenue", "Community Chest", "Pennsylvania Avenue", "Short Line", "Chance", "Park Place",
            "Luxury Tax", "Boardwalk" };

    private int[] landings; // ledger pointer
    private int[] ledger_1; // for n 1,000
    private int[] ledger_2; // for n 10,000
    private int[] ledger_3; // for n 100,000
    private int[] ledger_4; // for n 1,000,000

    public LandingLedger() {
        ledger_1 = new int[41];
        ledger_2 = new int[41];
        ledger_3 = new int[41];
        ledger_4 = new int[41];
        for (int i = 0; i < 41; i++) {
            ledger_1[i] = 0;
            ledger_2[i] = 0;
            ledger_3[i] = 0;
            ledger_4[i] = 0;
        }

        landings = ledger_1;
    }

    public void swap(int code) {
        switch (code) {
            case 1 -> {
                landings = ledger_1;
            }
            case 2 -> {
                landings = ledger_2;
            }
            case 3 -> {
                landings = ledger_3;
            }
            case 4 -> {
                landings = ledger_4;
            }
            default -> {
                System.out.println("Invalid ledger code (1-4). Default 1");
                landings = ledger_1;
            }
        }
    }

    public void landOn(int space) {
        landings[space]++;
    }

    public int get(int index) {
        return landings[index];
    }

    public void createCSV(String name) {
        new File("./output").mkdir(); // make output folder if not there already
        createFile(name);
    }

    private void createFile(String name) {
        String filename = "./output/" + name + ".csv";

        File file = new File(filename);
        try {
            if (file.createNewFile()) {
                System.out.println("Created new csv file named: " + name);
                writeFile(filename);
            } else
                System.out.println(name + ".csv already exists!");

        } catch (IOException e) {
            System.out.println("File creation error");
            e.printStackTrace();
        }
    }

    private void writeFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // calculate total moves in each of the four ledgers
            int[] sums = new int[4];
            for (int cd = 1; cd <= 4; ++cd) {
                swap(cd);
                sums[cd - 1] = 0;
                for (int space : landings) {
                    sums[cd - 1] += space;
                }
            }

            // write headers of file
            writer.write(HEADER_1);
            writer.write("\n");
            writer.write(HEADER_2);
            // write body of file
            StringBuilder line;
            Double percentage;
            for (int space = 1; space <= 40; space++) {
                line = new StringBuilder();

                line.append(SPACES[space - 1]); // space name
                for (int cd = 1; cd <= 4; cd++) { // for each n ledger
                    swap(cd);
                    line.append(',');
                    line.append(get(space)); // times landed on space
                    line.append(',');
                    percentage = (double) get(space) / sums[cd - 1] * 100;
                    line.append(String.format("%.2f%%", percentage)); // fraction of total landings
                }

                writer.write("\n");
                writer.write(line.toString());
            }

        } catch (IOException e) {
            System.out.println("File writing error");
            e.printStackTrace();
        }
    }
}