package words.g7;

import words.core.Letter;
import words.core.Player;
import words.core.PlayerBids;
import words.core.SecretState;

import java.util.*;

public class Group7Player extends Player {
    // set a max price
    private static final int MAX_BID = 15;
    private Random rand = new Random();

    @Override
    public int bid(Letter bidLetter, List<PlayerBids> playerBidList, int totalRounds,
                   ArrayList<String> playerList, SecretState secretstate, int playerID) {
        int currentScore = secretstate.getScore();
        int letterValue = bidLetter.getValue();
        int letterCount = getLetterCount(bidLetter.getCharacter());


        // determine the bid by the current score & letter value
        int bid = Math.min(currentScore, letterValue);

        // if a letter has 5 or less, bid more
        if (letterCount <= 5){
            bid += 2;
        }

        if(letterValue == 1){
            bid += 4;
        }else if(letterValue == 2){
            bid += 2;
        }else if(letterValue == 3){
            bid += 1;
        }else if(letterValue == 10) {
            bid -= 5;
        }else if(letterValue == 8) {
            bid -= 1;
        }


        // adjust bid based on bid history
        bid = adjustBidBasedOnHistory(bid, bidLetter, playerBidList);

        // adjust bid based on letter amount
        bid = adjustBidBasedOnCurrentLetters(bid);

        // make sure the bid is within a reasonable range
        bid = Math.min(bid, MAX_BID);
        bid = Math.max(bid, 2); //

        // add some random bid
        bid += rand.nextInt(3);

        if(currentScore <= 60){
            bid = Math.min(bid, 12);
        }

        if(currentScore <= 30){
            bid = Math.min(bid, 2);
        }

        return bid;
    }

    private int adjustBidBasedOnHistory(int initialBid, Letter bidLetter, List<PlayerBids> playerBidList) {
        // check the letter's bid history, if the bid was higher, make the bid 1 more than the second highest bid
        for (PlayerBids playerBid : playerBidList) {
            if (playerBid.getTargetLetter().equals(bidLetter)) {
                int highestBid = playerBid.getBidValues().stream().max(Integer::compare).orElse(0);
                int secondHighestBid = playerBid.getBidValues().stream().filter(bid -> bid != highestBid).max(Integer::compare).orElse(0);
                if (highestBid > initialBid) {
                    initialBid = secondHighestBid + 1;
                }
                break;
            }
        }
        return initialBid;
    }

    private int adjustBidBasedOnCurrentLetters(int bid){
        // if I have 7 or more letter, bid less
        if (myLetters.size() > 7) {
            bid -= (int) ((myLetters.size() - 7) * 1.5);
        }
        return bid;
    }

    private int getLetterCount(Character c) {
        return switch (c) {
            case 'J','K','Q','X','Z' -> 1;
            case 'B','C','F','H','M','P','V','W','Y' -> 2;
            case 'G' -> 3;
            case 'D', 'L', 'S', 'U' -> 4;
            case 'N','R','T' -> 6;
            case 'O' -> 8;
            case 'A', 'I' -> 9;
            case 'E' -> 12;
            default -> 0;
        };
    }
}




