import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Used for storing the spaces landed on during the Monopoly simulations. Is
 * capable of storing 4 "CDs" (arrays) of data for each of the N scale tests.
 * The landOn method adds a tally to the given space landed on.
 * 
 * @author Valor Goff
 */
public class LandingLedger {
    // strings for the CSV file generation
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
    private int[] ledger_1; // "CD" for n 1,000
    private int[] ledger_2; // "CD" for n 10,000
    private int[] ledger_3; // "CD" for n 100,000
    private int[] ledger_4; // "CD" for n 1,000,000

    /**
     * Constructor for landing ledger object. Intial values (tallies) for 4 "CD"
     * arrays are all 0 for all spaces.
     */
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

        landings = ledger_1; // sets default operating "CD" to ledger_1
    }

    /**
     * Swaps the target array the landOn and get command is used for, like popping
     * out and in a new CD.
     * 
     * @param code Which array (1-4) to swap between.
     */
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

    /**
     * Adds a tally to the given space in the current operating array.
     * 
     * @param space Which space to add a tally to in the array.
     */
    public void landOn(int space) {
        landings[space]++;
    }

    /**
     * Returns the number of tallies at a given space.
     * 
     * @param space which space to get tallies from.
     * @return
     */
    public int get(int space) {
        return landings[space];
    }

    /**
     * Generates a CSV file with a given name in the output folder. (Also generates
     * output folder at ./output next to src folder.) CSV has the tallies for all 4
     * simulations saved, for each space, and the percentage of total tallies made.
     * Does not create file if name is already taken.
     * 
     * @param name What to name the CSV file.
     */
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
                System.out.println(name + ".csv already exists! No file created.");

        } catch (IOException e) {
            System.out.println("File creation error");
            e.printStackTrace();
        }
    }

    private void writeFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // calculate total moves in each of the four ledgers (for percentage
            // calculation)
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