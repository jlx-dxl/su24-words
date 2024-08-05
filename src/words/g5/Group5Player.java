package words.g5;

import words.core.Letter;
import words.core.PlayerBids;
import words.core.SecretState;
import words.core.Word;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Group5Player extends words.core.Player {

    private HashMap<Character, Double> freqInWords = new HashMap<>();
    private HashMap<Character, Double> freqInLetters = new HashMap<>();

    private HashMap<Character, Double> playerLetterFreq = new HashMap<>();

    private boolean isVowel(char c) {
        return "AEIOUaeiou".indexOf(c) >= 0;
    }

    private double countOccurrences(char c) {
        double count = 0.0;
        Word[] wordList = super.wordlist;

        for (Word w : wordList) {
            String wString = w.word;
            for (int i=0; i<wString.length(); ++i) {
                if (c == '?') {
                    ++ count;
                }
                else if (c == wString.charAt(i)) {
                    ++count;
                }
            }
        }

        return count;
    }

    private void initFreqMaps() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        double[] freq = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

        double totalNumChars = countOccurrences('?');
        for (int i=0; i<26; ++i) {
            freqInLetters.put(chars[i], freq[i] / 98.0);
            double charFreq = countOccurrences(chars[i]);
            freqInWords.put(chars[i], charFreq / totalNumChars);
        }
    }

    @Override
    public int bid(Letter bidLetter, List<PlayerBids> playerBidList, int totalRounds, ArrayList<String> playerList, SecretState secretstate, int playerID) {
        initFreqMaps();
        Word[] wordList = super.wordlist;
        int id = super.myID;
        int numPlayers = super.numPlayers;
        List<Character> myLetters = super.myLetters;
        List<List<Character>> playerLetters = super.playerLetters;
        List<Integer> scores = super.scores;

        Character bidChar = bidLetter.getCharacter();
        int numChar = Collections.frequency(myLetters, bidChar);

        myLetters.add(bidChar);
        String bestWord = returnWord();

        int numCharInBest = Collections.frequency(Arrays.asList(bestWord.toCharArray()), bidChar);
        int bid;

        if (bestWord.contains(bidChar.toString()) && numChar == numCharInBest - 1) {
            if (bestWord.length() == 7) {
                bid = 25;
            }
            else {
                //bid = ScrabbleValues.getWordScore(bestWord) / 2 + 1
                bid = bidLetter.getValue() + 1;
            }
        }
        else if (isVowel(bidChar)) {
            bid = 4;
        }
        else {
            bid = 2;
        }

        myLetters.remove(bidChar);
        return Math.min(bid, secretstate.getScore());
    }
}
