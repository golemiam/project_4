package project4Monopoly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MonoCards {
	public static int commposition = -1;
	public static int cCposition = -1;

	public static void monopolycards() {		
		/*
		//Test outputs
		shuffleDeck(cCCards);
		System.out.println(cCCards);
		
		for (int i = 0; i < 27; i++) {
			System.out.print(drawComm(0) + ", ");
			if (commposition == 15) {
				System.out.println();
			}
		}
		
		System.out.println();
		System.out.println();
		shuffleDeck(cCards);
		System.out.println(cCards);
		
		for (int i = 0; i < 27; i++) {
			System.out.print(drawChance(0) + ", ");
			if (cCposition == 15) {
				System.out.println();
			}
		}
		
		//System.out.println();
		//System.out.println(CCShuffled);	
		 */
	}
	
	//Shuffles the deck of cards entered as a parameter.
	public static void shuffleDeck(List<Integer> deck) { 
		Collections.shuffle(deck);
	}
	
	//There are two separate methods for drawing cards. 
	//The decks are different enough that I felt making separate methods was justified.
	
	//Draws a card from the Community Chest Deck.
	public static int drawComm (int position, List<Integer> cCCards) {
		//Increment the Community Chest card we are looking at.
		commposition++;
		
		//If we look at a number outside the deck, we are at the end of the deck.
		//If we are at the end, change to look at first card and shuffle deck. (Basically resetting it)
		if(commposition >= 16) {
			commposition = 0;
			Collections.shuffle(cCCards);
			System.out.println(cCCards);
		}
		
		//Look that the value of the card we drew.
		int effect = cCCards.get(commposition);
		
		//Determines effect if there is an effect.
		switch(effect) {
			/*Where go is.*/
			case 1: 
				return 0;
			
			/*Get out of jail free card = true; no position change.*/
			case 4:
				jailcard(true);
				return position; 
				
			/*In Jail and move to where jail is.*/
			case 5:
				inJail(true);
				return 11;
				
			//No relevant effect on the card; nothing happens.
			default: 
				return position;
		}
	}
	
	public static int drawChance (int position, List<Integer> cCards) {
		//Increment the card we are looking at.
		cCposition++;
		
		//If we look at a number outside the deck, we are at the end of the deck
		//If we are at the end, change to look at first card and shuffle deck. (Basically resetting it.)
		if(cCposition >= 16) {
			cCposition = 0;
			Collections.shuffle(cCards);
			System.out.println(cCards);
		}
		
		//Look that the value of the card we drew.
		int effect = cCards.get(cCposition);
		
		//Determine if the card has an effect.
		switch(effect) {
		/*Where Boardwalk is.*/
		case 0: 
			return 40;
		
		/*Where go is.*/
		case 1: 
			return 0;
			
		/*Where Illinois Avenue is.*/
		case 2: 
			return 24;
		
		/*Where St. Charles position is.*/
		case 3: 
			return 12;
		
		/*Where the nearest Railroad is.*/
		case 4: 
			return nearestRailroad(position);
			
		/*Where the nearest Railroad is.*/
		case 5: 
			return nearestRailroad(position);
		
		/*Where nearest Utility is.*/	
		case 6: 
			return nearestUtility(position);
		
		/*Get out of jail free card = true.*/
		case 8: 
			jailcard(true);
			return position;
		
		/*Move back three spaces.*/
		case 9: 
			return position - 3;
		
		/*In jail and move to where jail is.*/
		case 10: 
			inJail(true);
			return 11;
		
		/*Where Reading railroad is.*/
		case 13: 
			return 6;
		
		/* Card had no relevant effect*/
		default: 
			return position;
		}
	}
	
	//Obtain a jail card, so change it to true.
	public static boolean jailcard(boolean hasCard) {
		return hasCard = true;
	}
	
	//Go to jail, so set inJail to true.
	public static boolean inJail(boolean inJail) {
		return inJail = true;
	}
	
	//Determines the closest railroad and changes output position to that space.
	public static int nearestRailroad(int position) {
		int newPosition = position;
		
		switch(position) {
		
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
			//System.out.println("This should never happen, probably an error.");
		}
		
		return newPosition;
	}
	
	//Determines the closest utility and changes output position to that space.
	public static int nearestUtility(int position) {
		int newPosition = position;
		
		switch(position) {
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
			//System.out.println("This should never happen, probably an error.");
		}
		
		return newPosition;
	}
	
	public static List<Integer> getChanceDeck(){
		//Hashmap of Chance cards, mostly for vanity to see what each card actually is.
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
				
		//Turing our KeyValuePair HashMap into an int array for optimization in the sim.
		List<Integer> cCards = new ArrayList<>(ChanceCards.keySet());
		
		shuffleDeck(cCards);
		
		return cCards;
	}
	
	//Initialize the Community Chest Deck for public use.
	public static List<Integer> getCommDeck(){
		//Hashmap of Community Chest cards, mostly for vanity to see what each card actually is.		
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
		
		//Turing our KeyValuePair HashMap into an int array for optimization in the sim.
		List<Integer> cCCards = new ArrayList<>(CommunityChestCards.keySet());
		
		shuffleDeck(cCCards);
		
		return cCCards;
	}
}
