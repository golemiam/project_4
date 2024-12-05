import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Main {
    static char strat; // A=fine, B=doubles
    static LandingLedger ledger;

    /**
     * Defining "landing" as any space you move to after the instant start of your
     * turn. If you land on a chance card you also land anywhere it moves you. If
     * you land on go to jail you also land on jail. Failure to escape from jail is
     * not landing on jail again.
     * 
     * @author Valor Goff
     */
    public static void main(String[] args) {
        // prep data storage
        LandingLedger ledgerA = new LandingLedger();
        LandingLedger ledgerB = new LandingLedger();
        // file name
        String nameEnder = "sim3";

        ledger = ledgerA;
        strat = 'A';
        int n_tests = 1000;
        for (int i = 1; i <= 4; ++i) {
            ledger.swap(i);
            play(n_tests);
            n_tests *= 10;
        }

        ledger = ledgerB;
        strat = 'B';
        n_tests = 1000;
        for (int i = 1; i <= 4; ++i) {
            ledger.swap(i);
            play(n_tests);
            n_tests *= 10;
        }

        // = = prints = =

        int sum = 0;
        System.out.println("A stats:");
        for (int i = 1; i <= 40; i++) {
            System.out.println(i + ": " + ledgerA.get(i));
            sum += ledgerA.get(i);
        }
        System.out.println("sum = " + sum);

        sum = 0;
        System.out.println("B stats:");
        for (int i = 1; i <= 40; i++) {
            System.out.println(i + ": " + ledgerB.get(i));
            sum += ledgerB.get(i);
        }
        System.out.println("sum = " + sum);

        // = = save = =
        ledgerA.createCSV("stratA" + nameEnder);
        ledgerB.createCSV("stratB" + nameEnder);
    }

    /**
     * Implementation by Robbie. Cleaned, Jail code, Landing code added by Valor.
     * 
     * @author Robbie
     */
    private static void play(int turns) {
        JailHandling.reset();
        Cards.reset();

        // objects
        Random random = new Random();
        // variables
        int position = 1; // 40 squares, [1-40], 1 = Start/Go
        int doublesInARow = 0;
        int n_turns = turns;
        boolean gotDoubles = false;

        // play game
        System.out.println("Start");
        for (int i = 1; i <= n_turns; i++) {
            // roll two six sided dice (rand [0-5]+1)
            int diceOne = random.nextInt(5) + 1;
            int diceTwo = random.nextInt(5) + 1;
            int totalRolled = diceTwo + diceOne;

            if (diceOne == diceTwo) {
                System.out.print("Doubles!: ");
                gotDoubles = true;
            } else
                gotDoubles = false;

            if (JailHandling.isJailed()) {
                JailHandling.bail(gotDoubles);
                if (JailHandling.isJailed()) // if still jailed
                    continue;
            }

            System.out.println(position + " + " + totalRolled + " = " + (position + totalRolled));

            // you do not get bonus if you used doubles to escape
            if (JailHandling.usedDoubles) {
                // reset marker
                JailHandling.usedDoubles = false;
            }
            // check for doubles and its bonus/effects
            else {
                if (gotDoubles) {
                    doublesInARow++;

                    // (first or second doubles) bonus "turn"
                    if (doublesInARow < 3) {
                        // since this is single player the bonus move can just be an extra turn
                        n_turns++;
                    }
                    // (third doubles in a row) jailed
                    else {
                        System.out.println("Youch!");
                        JailHandling.jail();
                        position = 11; // jailed
                        ledger.landOn(position);
                        doublesInARow = 0; // reset tracker
                        continue; // end of turn
                    }
                } else
                    doublesInARow = 0; // reset
            }

            // move forward
            if (position <= 40) {
                position += totalRolled;
            }
            // loop around if you pass go
            if (position > 40) {
                position -= 40; // subtracted (not reset) incase you overshoot
            }
            ledger.landOn(position);

            // check if on chance space
            if (position == 8 || position == 23 || position == 37) {
                System.out.println("(Chance)");

                int newPos = Cards.drawChance(position);
                if (newPos != position)
                    ledger.landOn(newPos);
                position = newPos;
            }
            // check if on community chest space
            if (position == 3 || position == 18 || position == 34) {
                System.out.println("(Commun)");

                int newPos = Cards.drawCommunity(position);
                if (newPos != position)
                    ledger.landOn(newPos);
                position = newPos;
            }
            // landed on go to jail (31)
            if (position == 31) {
                JailHandling.jail();
                position = 11;
                ledger.landOn(position);
            }

            System.out.println(position);
        }
        System.out.println("End");
    }

    /**
     * @author Valor Goff.
     */
    private class JailHandling {
        private static boolean hasGetOutOfJailFree;
        private static boolean jailed;
        private static char stratagy;
        private static int turnsInJail;
        // messy solution to prevent the doubles bonus after escaping
        public static boolean usedDoubles;

        public static void reset() {
            hasGetOutOfJailFree = false;
            jailed = false;
            stratagy = strat;
            turnsInJail = 0;
            usedDoubles = false;
        }

        public static void bail(boolean hasDoubles) {
            turnsInJail++;
            switch (stratagy) {
                case 'A' -> {
                    tryCard();
                    payFine(); // technically this would be paid before roll, but makes no difference here
                }
                case 'B' -> {
                    tryCard();
                    tryDoubles(hasDoubles);
                }
            }
        }

        private static void tryDoubles(boolean hasDoubles) {
            if (hasDoubles) {
                System.out.println("Broke out!");
                usedDoubles = true;
                getOut();
            }
            // if third failure to roll doubles
            if (turnsInJail >= 3) {
                payFine();
            }
        }

        private static void payFine() {
            System.out.println("Paid fine...");
            getOut();
        }

        private static void tryCard() {
            if (hasGetOutOfJailFree) {
                System.out.println("Used card.");

                hasGetOutOfJailFree = false;
                getOut();
            }

        }

        private static void getOut() {
            jailed = false;
            turnsInJail = 0;
        }

        public static void giveCard() {
            hasGetOutOfJailFree = true;
        }

        public static void jail() {
            jailed = true;
        }

        public static boolean isJailed() {
            return jailed;
        }
    }

    /**
     * Implemented by Damian. Commented by Valor Goff.
     * 
     * @author Damian
     */
    private class Cards {
        private static int communityPos;
        private static int chancePos;
        private static List<Integer> CommunityDeck;
        private static List<Integer> ChanceDeck;

        public static void reset() {
            communityPos = -1;
            chancePos = -1;
        }

        // There are two separate methods for drawing cards.
        // The decks are different enough that I felt making separate methods was
        // justified.

        /*
         * "Draws" a card from the Community Chest Deck and computes the "card's"
         * effect, returning the position.
         */
        public static int drawCommunity(int playerPos) {
            if (communityPos == -1) {
                CommunityDeck = getCommunityDeck();
                Collections.shuffle(CommunityDeck);

                System.out.println(CommunityDeck);
            }

            // Look at (draw) next card of community chest.
            communityPos++;

            // If we look at a number outside the deck, we ran out of deck.
            // If at end, shuffle and look at first card (resetting deck).
            if (communityPos >= 16) {
                Collections.shuffle(CommunityDeck);
                communityPos = 0;

                System.out.println(CommunityDeck);
            }

            int card = CommunityDeck.get(communityPos);
            switch (card) {
                // Go to 'go' square.
                case 1:
                    return 0;

                // Give 'get out of jail free' card.
                case 4:
                    JailHandling.giveCard();
                    return playerPos; // no pos change

                // Get Jailed.
                case 5:
                    JailHandling.jail();
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
        public static int drawChance(int playerPos) {
            if (chancePos == -1) {
                ChanceDeck = getChanceDeck();
                Collections.shuffle(ChanceDeck);

                System.out.println(ChanceDeck);
            }

            // Look at (draw) next card of chance deck.
            chancePos++;

            // If we look at a number outside the deck, we ran out of deck.
            // If at end, shuffle and look at first card (resetting deck).
            if (chancePos >= 16) {
                Collections.shuffle(ChanceDeck);
                chancePos = 0;

                System.out.println(ChanceDeck);
            }

            int card = ChanceDeck.get(chancePos);
            switch (card) {
                // Go to 'Boardwalk' square.
                case 0:
                    return 40;

                // Go to 'go' square.
                case 1:
                    return 1;

                // Go to 'Illinois Avenue' square.
                case 2:
                    return 25;

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
                    JailHandling.giveCard();
                    return playerPos; // no pos change

                // Go back three spaces.
                case 9:
                    // quirk of this project, jumping to a position doesn't need to be processed,
                    // except for chance-37 this case. This edge case also lands on a community
                    // chest, but due to our structure this is already processed and doesn't need to
                    // be implemented. -V
                    return playerPos - 3;

                // Get Jailed.
                case 10:
                    JailHandling.jail();
                    return 11;

                // Go to 'Reading Railroad' square.
                case 13:
                    return 6;

                // Card has no relevant effect.
                default:
                    return playerPos; // no pos change
            }
        }

        // From chance spaces (8,23,37) to nearest railroad.
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

        // From chance spaces (8,23,37) to nearest utility.
        public static int nearestUtility(int playerPos) {
            int newPosition = playerPos;

            switch (playerPos) {
                case 8:
                    newPosition = 13;
                    break;
                case 23:
                    newPosition = 29;
                    break;
                case 37:
                    newPosition = 13;
                    break;
                default:
                    System.out.println("Nearest Utility error, switch = default"); // should never happen
                    return playerPos;
            }

            return newPosition;
        }

        private static List<Integer> getChanceDeck() {
            // Hashmap of Chance cards (strings are for fun, not actually used)
            HashMap<Integer, String> fullChanceCards = new HashMap<Integer, String>();
            fullChanceCards.put(0, "Advance to Boardwalk.");
            fullChanceCards.put(1, "Advance to Go!");
            fullChanceCards.put(2, "Advance to Illinois Avenue.");
            fullChanceCards.put(3, "Advance to St. Charles position.");
            fullChanceCards.put(4, "Advance to the nearest Railroad.");
            fullChanceCards.put(5, "Advance to the nearest Railroad.");
            fullChanceCards.put(6, "Advance token to nearest Utility.");
            fullChanceCards.put(7, "Bank pays you dividend");
            fullChanceCards.put(8, "Get Out of Jail Free");
            fullChanceCards.put(9, "Go Back 3 Spaces");
            fullChanceCards.put(10, "Go to Jail.");
            fullChanceCards.put(11, "General Repairs");
            fullChanceCards.put(12, "Speeding fine");
            fullChanceCards.put(13, "Take a trip to Reading Railroad.");
            fullChanceCards.put(14, "Chairman of the Board.");
            fullChanceCards.put(15, "Building Loan");

            // reducing down to int array
            List<Integer> chanceCards = new ArrayList<>(fullChanceCards.keySet());
            return chanceCards;
        }

        private static List<Integer> getCommunityDeck() {
            // Hashmap of Community Chest cards (strings are for fun, not actually used)
            HashMap<Integer, String> fullCommunityCards = new HashMap<Integer, String>();
            fullCommunityCards.put(0, "Advance to Go!");
            fullCommunityCards.put(1, "Bank Error.");
            fullCommunityCards.put(2, "Doctor Fee.");
            fullCommunityCards.put(3, "Sale of Stock.");
            fullCommunityCards.put(4, "Get out of Jail Free.");
            fullCommunityCards.put(5, "Go to Jail.");
            fullCommunityCards.put(6, "Holiday funds mature.");
            fullCommunityCards.put(7, "Income Tax Refund.");
            fullCommunityCards.put(8, "Birthday!");
            fullCommunityCards.put(9, "Life Insurance.");
            fullCommunityCards.put(10, "Pay Hospital.");
            fullCommunityCards.put(11, "Pay School Fees.");
            fullCommunityCards.put(12, "Consiltancy Fee.");
            fullCommunityCards.put(13, "Assessed Street Repair.");
            fullCommunityCards.put(14, "Won second prize.");
            fullCommunityCards.put(15, "Inheiritence.");

            // reducing down to int array
            List<Integer> communityChestCards = new ArrayList<>(fullCommunityCards.keySet());
            return communityChestCards;
        }
    }
}
