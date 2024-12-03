import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.Random;

public class Main {
	public static int commPosition = -1;
	public static int chestPosition = -1;
	public static boolean Jailcard = false;
	public static boolean inJail = false;

	public static void main(String[] args) throws IOException {
		
		String inputFile = "";
		String outputFile = "sorted_data.csv";
		int playerPosition = 0;
		Random random = new Random();

		int amountOfRolls = 100;

		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				FileWriter writer = new FileWriter(outputFile)) {

			List<String> lines = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}

			for (int i = 1; i <= amountOfRolls; i++) {
				String str1 = String.valueOf(playerPosition);
				System.out.println(str1);
				int diceOne = random.nextInt(5) + 1; // Generates a random integer between 0 and 9 (inclusive)
				int diceTwo = random.nextInt(5) + 1;
				int totalRolled = diceTwo + diceOne;
				if (playerPosition == 8 || playerPosition == 23 || playerPosition == 37) {
					System.out.println("Chance");
					playerPosition = drawChance(playerPosition, getChanceDeck());
				}
				
				if (playerPosition == 3 || playerPosition == 18 || playerPosition == 34) {
					System.out.println("Community Chest");
					playerPosition = drawComm(playerPosition, getCommDeck());
				}
				
				/*
				//Other way of player position
				playerPosition += totalRolled;
				playerPosition = playerPosition % 40;
				*/
				
				//Original player positions
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
			}
			;
			
			// Sort by the first column (splitting by comma)
			// Collections.sort(lines, Comparator.comparing(l -> l.split(",")[0]));
			/*
			 * int columnIndex = 1; // Index of the column you want to extract (starting
			 * from 0) // Using CSVReader to print values from the specified column try
			 * (CSVReader csvReader = new CSVReader(new FileReader(inputFile))) {
			 * List<String[]> records = csvReader.readAll();
			 * 
			 * for (String[] record : records) { if (record.length > columnIndex) {
			 * System.out.println(record[columnIndex]); } } } catch (CsvException e) {
			 * e.printStackTrace(); } String str1 = String.valueOf(playerPosition);
			 * System.out.println(str1);
			 * 
			 * for (String l : lines) { writer.write(l + "\n"); }
			 */
		}
	}

	/* GENERAL GAME MECHANICS*/
	public static void JailStrategyA() {

	}
	
	public static void JailStrategyB() {

	}

	
	/* COMMUNITY CHEST CARDS AND CHANCE CARDS */
	// Draws a card from the Community Chest Deck.
	public static int drawComm(int position, List<Integer> cCCards) {
		// Increment the Community Chest card we are looking at.
		commPosition++;

		// If we look at a number outside the deck, we are at the end of the deck.
		// If we are at the end, change to look at first card and shuffle deck.
		// (Basically resetting it)
		if (commPosition >= 16) {
			commPosition = 0;
			Collections.shuffle(cCCards);
			System.out.println(cCCards);
		}

		// Look that the value of the card we drew.
		int effect = cCCards.get(commPosition);

		// Determines effect if there is an effect.
		switch (effect) {
		/* Where go is. */
		case 1:
			return 0;

		/* Get out of jail free card = true; no position change. */
		case 4:
			Jailcard = true;
			return position;

		/* In Jail and move to where jail is. */
		case 5:
			inJail = true;
			return 31;

		// No relevant effect on the card; nothing happens.
		default:
			return position;
		}
	}

	public static int drawChance(int position, List<Integer> cCards) {
		// Increment the card we are looking at.
		chestPosition++;

		// If we look at a number outside the deck, we are at the end of the deck
		// If we are at the end, change to look at first card and shuffle deck.
		// (Basically resetting it.)
		if (chestPosition >= 16) {
			chestPosition = 0;
			Collections.shuffle(cCards);
			System.out.println(cCards);
		}

		// Look that the value of the card we drew.
		int effect = cCards.get(chestPosition);

		// Determine if the card has an effect.
		switch (effect) {
		/* Where Boardwalk is. */
		case 0:
			return 40;

		/* Where go is. */
		case 1:
			return 0;

		/* Where Illinois Avenue is. */
		case 2:
			return 24;

		/* Where St. Charles position is. */
		case 3:
			return 12;

		/* Where the nearest Railroad is. */
		case 4:
			return nearestRailroad(position);

		/* Where the nearest Railroad is. */
		case 5:
			return nearestRailroad(position);

		/* Where nearest Utility is. */
		case 6:
			return nearestUtility(position);

		/* Get out of jail free card = true. */
		case 8:
			Jailcard = true;
			return position;

		/* Move back three spaces. */
		case 9:
			return position - 3;

		/* In jail and move to where jail is. */
		case 10:
			inJail = true;
			return 31;

		/* Where Reading railroad is. */
		case 13:
			return 6;

		/* Card had no relevant effect */
		default:
			return position;
		}
	}

	// Only called from chance card.
	// Determines the closest railroad and changes output position to that space.
	public static int nearestRailroad(int position) {
		int newPosition = position;

		switch (position) {

		case 8:
			newPosition = 16;
			break;

		case 23:
			newPosition = 26;
			break;

		case 38:
			newPosition = 6;
			break;

		default:
			break;
		// System.out.println("This should never happen, probably an error.");
		}

		return newPosition;
	}

	// Only called from the chance card.
	// Determines the closest utility and changes output position to that space.
	public static int nearestUtility(int position) {
		int newPosition = position;

		switch (position) {
		case 8:
			newPosition = 13;
			break;
		case 23:
			newPosition = 29;
			break;
		case 38:
			newPosition = 13;
			break;
		default:
			// System.out.println("This should never happen, probably an error.");
		}

		return newPosition;
	}

	public static List<Integer> getChanceDeck() {
		// Hashmap of Chance cards, mostly for vanity to see what each card actually is.
		HashMap<Integer, String> ChanceCards = new HashMap<Integer, String>();
		ChanceCards.put(0, "Advance to Boardwalk.");
		ChanceCards.put(1, "Advance to Go!");
		ChanceCards.put(2, "Advance to Illinois Avenue.");
		ChanceCards.put(3, "Advance to St. Charles position.");
		ChanceCards.put(4, "Advance to the nearest Railroad.");
		ChanceCards.put(5, "Advance to the nearest Railroad.");
		ChanceCards.put(6, "Advance token to nearest Utility.");
		ChanceCards.put(7, "Bank pays you dividend");
		ChanceCards.put(8, "Get Out of Jail Free");
		ChanceCards.put(9, "Go Back 3 Spaces");
		ChanceCards.put(10, "Go to Jail.");
		ChanceCards.put(11, "General Repairs");
		ChanceCards.put(12, "Speeding fine");
		ChanceCards.put(13, "Take a trip to Reading Railroad.");
		ChanceCards.put(14, "Chairman of the Board.");
		ChanceCards.put(15, "Building Loan");

		// Turing our KeyValuePair HashMap into an int array for optimization in the
		// sim.
		List<Integer> cCards = new ArrayList<>(ChanceCards.keySet());

		Collections.shuffle(cCards);

		return cCards;
	}

	// Initialize the Community Chest Deck for public use.
	public static List<Integer> getCommDeck() {
		// Hashmap of Community Chest cards, mostly for vanity to see what each card
		// actually is.
		HashMap<Integer, String> CommunityChestCards = new HashMap<Integer, String>();
		CommunityChestCards.put(0, "Advance to Go!");
		CommunityChestCards.put(1, "Bank Error.");
		CommunityChestCards.put(2, "Doctor Fee.");
		CommunityChestCards.put(3, "Sale of Stock.");
		CommunityChestCards.put(4, "Get out of Jail Free.");
		CommunityChestCards.put(5, "Go to Jail.");
		CommunityChestCards.put(6, "Holiday funds mature.");
		CommunityChestCards.put(7, "Income Tax Refund.");
		CommunityChestCards.put(8, "Birthday!");
		CommunityChestCards.put(9, "Life Insurance.");
		CommunityChestCards.put(10, "Pay Hospital.");
		CommunityChestCards.put(11, "Pay School Fees.");
		CommunityChestCards.put(12, "Consiltancy Fee.");
		CommunityChestCards.put(13, "Assessed Street Repair.");
		CommunityChestCards.put(14, "Won second prize.");
		CommunityChestCards.put(15, "Inheiritence.");

		// Turing our KeyValuePair HashMap into an int array for optimization in the
		// sim.
		List<Integer> cCCards = new ArrayList<>(CommunityChestCards.keySet());

		Collections.shuffle(cCCards);

		return cCCards;
	}

}
