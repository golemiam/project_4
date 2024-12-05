import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Edited by Valor Goff
 * 
 * @author Damian
 */
public class MonoCards {
	public static int communityPos = -1;
	public static int chancePos = -1;

	// Shuffles the deck of cards entered as a parameter.
	public static void shuffleDeck(List<Integer> deck) {
		Collections.shuffle(deck);
	}

	// There are two separate methods for drawing cards.
	// The decks are different enough that I felt making separate methods was
	// justified.

	/*
	 * "Draws" a card from the Community Chest Deck and computes the "card's"
	 * effect, returning the position.
	 */
	public static int drawCommunity(int playerPos, List<Integer> communityCards) {
		// Look at (draw) next card of community chest.
		communityPos++;

		// If we look at a number outside the deck, we ran out of deck.
		// If at end, shuffle and look at first card (resetting deck).
		if (communityPos >= 16) {
			Collections.shuffle(communityCards);
			communityPos = 0;
			System.out.println(communityCards);
		}

		int card = communityCards.get(communityPos);
		switch (card) {
			// Go to 'go' square.
			case 1:
				return 0;

			// Give 'get out of jail free' card.
			case 4:
				giveJailCard(true);
				return playerPos; // no pos change

			// Go to 'jail' square.
			case 5:
				inJail(true); // set jail status
				return 11;

			// Card has no relevant effect.
			default:
				return playerPos; // no pos change
		}
	}

	/*
	 * "Draws" a card from the Chance Deck and computes the "card's" effect,
	 * returning the position.
	 */
	public static int drawChance(int playerPos, List<Integer> chanceCards) {
		// Look at (draw) next card of chance deck.
		chancePos++;

		// If we look at a number outside the deck, we ran out of deck.
		// If at end, shuffle and look at first card (resetting deck).
		if (chancePos >= 16) {
			chancePos = 0;
			Collections.shuffle(chanceCards);
			System.out.println(chanceCards);
		}

		int card = chanceCards.get(chancePos);
		switch (card) {
			// Go to 'Boardwalk' square.
			case 0:
				return 40;

			// Go to 'go' square.
			case 1:
				return 0;

			// Go to 'Illinois Avenue' square.
			case 2:
				return 24;

			// Go to 'St. Charles' square.
			case 3:
				return 12;

			// Go to nearest Railroad Square.
			case 4, 5:
				return nearestRailroad(playerPos);

			// Go to nearest Utility Square.
			case 6:
				return nearestUtility(playerPos);

			// Give 'get out of jail free' card.
			case 8:
				giveJailCard(true);
				return playerPos; // no pos change

			// Go back three spaces.
			case 9:
				return playerPos - 3;

			// Go to jail.
			case 10:
				inJail(true); // set jail status
				return 11;

			// Go to 'Reading Railroad' square.
			case 13:
				return 6;

			// Card has no relevant effect.
			default:
				return playerPos; // no pos change
		}
	}

	// TODO: Figure out?
	// Obtain a jail card, so change it to true.
	public static boolean giveJailCard(boolean hasCard) {
		return hasCard = true;
	}

	// TODO: Figure out?
	// Go to jail, so set inJail to true.
	public static boolean inJail(boolean inJail) {
		return inJail = true;
	}

	// Determines the closest railroad (from chance space) and changes output
	// position to that space.
	public static int nearestRailroad(int playerPos) {
		int newPosition = playerPos;

		switch (playerPos) {

			case 8:
				newPosition = 16;
				break;

			case 23:
				newPosition = 26;
				break;

			case 37:
				newPosition = 6;
				break;

			default:
				System.out.println("Nearest Railroad error, switch = default"); // should never happen
				return playerPos;
		}

		return newPosition;
	}

	// Determines the closest utility (from chance space) and changes output
	// position to that space.
	public static int nearestUtility(int playerPos) {
		int newPosition = playerPos;

		switch (playerPos) {
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
				System.out.println("Nearest Railroad error, switch = default"); // should never happen
				return playerPos;
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

		shuffleDeck(cCards);

		return cCards;
	}

	public static List<Integer> getCommunityDeck() {
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

		shuffleDeck(cCCards);

		return cCCards;
	}
}
