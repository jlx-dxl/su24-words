package words.g3;

import words.core.*;

import java.util.*;


public class Group3Player extends Player {
    Map<Integer, Map<Letter, Integer>> playerCounts;             //format: Map <PlayerID <letter_type, num_letters_of_type>
    Map<Letter, Integer> absoluteCounts;                         //format: Map <Letter, num_instances_on_board>
    int totalPlayed;
    ArrayList<String> vowels;
    ArrayList<String> easyConst; // value of 4 or fewer
    ArrayList<String> hardConst; // value of 5 or higher

    public Group3Player() {
        playerCounts = new HashMap<>();
        absoluteCounts = new HashMap<>();
        totalPlayed = 0;
        vowels = new ArrayList<>(Arrays.asList("A", "E", "I", "O", "U"));
        easyConst = new ArrayList<>(Arrays.asList(
                "B", "C", "D", "F", "G", "H", "L", "M", "N", "P", "R", "S", "T", "V", "W", "Y"
        ));
        hardConst = new ArrayList<>(Arrays.asList("J", "K", "Q", "X", "Z"));


    }

    /**
     * Calculates the probability of a letter coming up in the next round
     * @param letter the letter that you want to investigate
     * @return the probability of that word occurring in the next round, expressed as a double.
     */
    private double probability(Letter letter) {

        int allRemaining = 98 - totalPlayed;
        int thisRemaining = ScrabbleValues.getLetterFrequency(letter.getCharacter()) - getCount(letter);

        return thisRemaining/allRemaining;
    }

    /**
     * Keeps track of the letters held by each player on the board, excluding hidden letters.
     * Note that this is intended to be called each round, to keep a running tally of letters.
     * @param letter the letter that you want to record
     * @param ownerID the owner of that letter
     */
    private void recordLetter(Letter letter, int ownerID) {

        //check data
        if (letter == null) return;     //case letter is invalid
        if (ownerID < 0) return;       //case no one won this bid

        //if here, letter is valid

        // Add to the player count
        if (!playerCounts.containsKey(ownerID)) playerCounts.put(ownerID, new HashMap<Letter, Integer>());      //case never seen this player win
        else if (!playerCounts.get(ownerID).containsKey(letter)) playerCounts.get(ownerID).put(letter, 1);      //case this player has no letters of this type
        else playerCounts.get(ownerID).put(letter, (playerCounts.get(ownerID).get(letter)+1));                  //case this player already has a letter of this type

        //Add to the absolute count
        if(!absoluteCounts.containsKey(letter)) absoluteCounts.put(letter, 1);
        else absoluteCounts.put(letter, absoluteCounts.get(letter) + 1);

        //add to total seen
        totalPlayed++;

    }

    /**
     * Returns the num letters of this type held by a SPECIFIC player, excluding hidden letters.
     * Note that this does not include hidden letters.
     * @param playerID the player that you would like to see the hand for
     * @return a map representing the num letters of each type held by this player.
     * returns 0 if this record cannot be found.
     */
    private int getCount(int playerID, Letter letter) {

        //check data
        if (!playerCounts.containsKey(playerID) || !playerCounts.get(playerID).containsKey(letter)) return 0;
        return playerCounts.get(playerID).get(letter);
    }

    /**
     * Returns the num letters of this type held by ANY player, excluding hidden letters.
     * @param letter the letter that you wish to return a count for
     * @return  the number of instances of that letter that are confirmed to be 'in play' (i.e. in our hand, or the hand of another player)
     * returns 0 if this record cannot be found.
     */
    private int getCount(Letter letter) {
        if (!absoluteCounts.containsKey(letter)) return 0;
        return absoluteCounts.get(letter);
    }

    /**
     * returns the details of the letters held by a specific player
     * @param playerID the player that is holding the letters
     * @return a map in the form <letter, num_instances_held_by_player>
     */
    private Map<Letter, Integer> getLetters(int playerID) {
        return playerCounts.get(playerID);
    }


    public static boolean containsAllLetters(String str, List<Character> letters) {
        for (char letter : letters) {
            if (str.indexOf(letter) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param bidLetter the Letter currently up for bidding on
     * @param playerBidList an unmodifiable list of previous bids from this game
     * @param totalRounds the total number of rounds in the game, which is different from the current round
     * @param playerList list of all player names
     * @param secretstate set of data that is stored unique to each player (their score and secret letters)
     * @param playerID the ID of the player being asked to bid (can ignore)
     * @return
     */
    @Override
    public int bid(Letter bidLetter, List<PlayerBids> playerBidList, int totalRounds, ArrayList<String> playerList, SecretState secretstate, int playerID) {

        String word = returnWord();
        if (word.length() >= 7){
            return 0;
        }

        /*
        if(myLetters.size() >= 4){
            int words = 0;
            int words_with_new_let = 0;
            for (Word w : wordlist) {
                if(containsAllLetters(w.toString(), myLetters)){
                    words ++;
                    if(w.toString().contains(String.valueOf(bidLetter.getCharacter()))){
                        words_with_new_let ++;
                    }

                }
            }
            float prob = words_with_new_let / words;

            if (prob > 0.7){
                return 8;
            }
            if (prob > 0.5){
                return 6;
            }
            if (prob > 0.25){
                return 2;
            }
            if (prob > 0.1){
                return 0;
            }


        }
        */




        if (vowels.contains(String.valueOf(bidLetter.getCharacter()))){
            return 6;
        }
        if (easyConst.contains(String.valueOf(bidLetter.getCharacter()))){
            return 5;
        }
        if (hardConst.contains(String.valueOf(bidLetter.getCharacter()))){
            return 4;
        }

        //record the new information from the previous round ONLY (rest should already be cached)
        int lastIndex = playerBidList.size() - 1;
        if (playerBidList.size() > 0) recordLetter(playerBidList.get(lastIndex).getTargetLetter(), playerBidList.get(lastIndex).getWinnerID());

        return 0;
    }
}
