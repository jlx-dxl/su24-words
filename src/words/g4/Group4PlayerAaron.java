package words.g4;

import words.core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;

public class Group4PlayerAaron extends Player {
    private final Set<Character> VOWELS = new HashSet<>(Arrays.asList('A', 'E', 'I', 'O', 'U'));
    private Map<Character, Integer> topCharacterMap = new HashMap<>();
    private Map<String, Word> targetWords = new HashMap<>();
    private ArrayList<String> top25;

    @Override
    public void startNewGame(int playerID, int numPlayers) {
        myID = playerID; // store my ID

        // overriding contents
        newInitializeWordList(); // read the file containing all the words
        this.numPlayers = numPlayers; // so we know how many players are in the game
    }

    protected void newInitializeWordList() {
        String line = null;
        ArrayList<Word> wtmp = new ArrayList<>(55000);
        top25 = new ArrayList<>();

        try {
            BufferedReader r = new BufferedReader(new FileReader("files/wordlist.txt"));
            while (null != (line = r.readLine())) {
                wtmp.add(new Word(line.trim()));
                if (line.length() > 6) {
                    targetWords.put(line, new Word(line));

                    if (top25.size() < 25) {
                        top25.add(line);
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred.", e);
        }
        wordlist = wtmp.toArray(new Word[0]);
    }


    /**
     * Method is called when it is this player's turn to submit a bid. They should guarantee that
     * the value they return is between 0 and secretstate.getScore(). The highest bid wins, but
     * the winner only pays the price of the second-highest bid.
     *
     * This implementation bids a random amount up to 1/8 of its score
     *
     * @param bidLetter the Letter currently up for bidding on
     * @param playerBidList an unmodifiable list of previous bids from this game
     * @param totalRounds the total number of rounds in the game, which is different from the current round
     * @param playerList list of all player names
     * @param secretstate set of data that is stored unique to each player (their score and secret letters)
     * @param playerID the ID of the player being asked to bid (can ignore)
     * @return the amount to bid for the letter
     */
    @Override
    public int bid(Letter bidLetter, List<PlayerBids> playerBidList,
                   int totalRounds, ArrayList<String> playerList,
                   SecretState secretstate, int playerID) {

        // TODO: implement own bid
        List<Character> myList = getCurrentList();

        int numVal = 0;
        for (char c : myList) {
            if (VOWELS.contains(c)) {
                numVal++;
                System.out.print(c + ", ");
            }
        }

        System.out.println();

        double bonusWeight = 0.0;

        if (!topCharacterMap.isEmpty()) {
            if (topCharacterMap.containsKey(bidLetter.getCharacter())) bonusWeight += 0.05;
        }

        // add importance to vowels when there are less than 3
        if (numVal < 3 && VOWELS.contains(bidLetter.getCharacter()))
            return placeBid(0.35 + bonusWeight, bidLetter, secretstate);

        if (myLetters.size() > 7) return placeBid(0.13 - bonusWeight, bidLetter, secretstate);

        return placeBid(0.13 + bonusWeight, bidLetter, secretstate);
    }


    private double letterImportance(Letter bidLetter) {
        return 0.0;
    }

    /**
     * get current list of letters bet
     * */
    private List<Character> getCurrentList() {
        return myLetters;
    }

    /**
     * @param percent: between 0-1
     * @param bidLetter: letter on bid right now
     *
     * */
    private int placeBid(double percent, Letter bidLetter, SecretState secretstate) {
        double random = Math.random();

        while (random < percent) {
            random = Math.random();
        }

        return (int) ( random * secretstate.getScore() / 8);
    }

    @Override
    public void bidResult(PlayerBids currentBids){
        // update the List of which player has which letters
        char currChar = currentBids.getTargetLetter().getCharacter();

        playerLetters.get(currentBids.getWinnerID()).add(currChar);

        // see if I won the bid and add it to my letters if so
        if (myID == currentBids.getWinnerID()) {
            myLetters.add(currChar);

            StringBuilder currString = new StringBuilder();
            for (String s : targetWords.keySet()) {
                for (char c : s.toCharArray()) {
                    if (c != currChar) currString.append(c);
                }
            }

            List sortedKeys = new ArrayList(targetWords.keySet());
            Collections.sort(sortedKeys);

            for (int i = 0; i < 25; i++) {
                top25.set(i, sortedKeys.get(i).toString());
            }

            updateCharacterMap();
        }
    }

    private void updateCharacterMap() {
        for (String s : top25) {
            for (char c : s.toCharArray()) {
                if (!topCharacterMap.containsKey(c)) topCharacterMap.put(c, 1);
                else topCharacterMap.put(c, topCharacterMap.get(c) + 1);
            }
        }
    }
}
