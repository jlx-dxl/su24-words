package words.g1;
import words.core.*;
import java.util.*;

public class SamPlayerV0 extends Player {
    private Map<Character, Integer> letterFrequency;
    private Set<String> commonDoublets;
    private Set<String> commonTriplets;
    private Set<Character> vowels;
    private int currentRound;
    private int totalRounds;
    private Random random = new Random();

    @Override
    public void startNewGame(int playerID, int numPlayers) {
        super.startNewGame(playerID, numPlayers);
        initializeLetterFrequency();
        initializeCommonCombinations();
        initializeVowels();
        currentRound = 0;
    }

    @Override
    public void startNewRound(SecretState secretstate) {
        super.startNewRound(secretstate);
        currentRound++;
    }

    @Override
    public int bid(Letter bidLetter, List<PlayerBids> playerBidList,
                   int totalRounds, ArrayList<String> playerList,
                   SecretState secretstate, int playerID) {
        this.totalRounds = totalRounds;
        int letterValue = calculateLetterValue(bidLetter);
        int budget = calculateBidBudget(secretstate.getScore(), totalRounds - currentRound);

        // Calculate combination bonus
        int combinationBonus = calculateCombinationBonus(bidLetter);

        // Check consonant-to-vowel ratio
        int vowelBonus = calculateVowelBonus(bidLetter);


        int baseBid = Math.min((letterValue + combinationBonus + vowelBonus) / 2, budget);


        int randomFactor = random.nextInt(3) - 1; // Random number between -1 and 1
        int finalBid = Math.max(0, Math.min(baseBid + randomFactor, secretstate.getScore()));

        return finalBid;
    }

    private void initializeLetterFrequency() {
        letterFrequency = new HashMap<>();
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int[] frequencies = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        for (int i = 0; i < letters.length(); i++) {
            letterFrequency.put(letters.charAt(i), frequencies[i]);
        }
    }

    private void initializeCommonCombinations() {
        commonDoublets = new HashSet<>(Arrays.asList(
                "TH", "HE", "AN", "IN", "ER", "ON", "RE", "ED", "ND", "HA", "AT", "EN", "ES", "OF", "OR", "NT", "EA", "TI", "TO", "IT"
        ));

        commonTriplets = new HashSet<>(Arrays.asList(
                "THE", "AND", "ING", "ENT", "ION", "HER", "FOR", "THA", "NTH", "INT", "ERE", "TIO", "TER", "EST", "ERS", "ATI", "HAT", "ATE", "ALL", "ETH"
        ));
    }

    private void initializeVowels() {
        vowels = new HashSet<>(Arrays.asList('A', 'E', 'I', 'O', 'U'));
    }

    private int calculateLetterValue(Letter letter) {
        int scrabbleValue = letter.getValue();
        int frequency = letterFrequency.getOrDefault(letter.getCharacter(), 0);

        return scrabbleValue * 2 + (13 - frequency);
    }

    private int calculateCombinationBonus(Letter letter) {
        int bonus = 0;
        char c = letter.getCharacter();

        for (Character myLetter : myLetters) {
            String doublet = "" + c + myLetter;
            String reversedDoublet = "" + myLetter + c;
            if (commonDoublets.contains(doublet) || commonDoublets.contains(reversedDoublet)) {
                bonus += 3;
            }

            for (Character otherLetter : myLetters) {
                if (!myLetter.equals(otherLetter)) {
                    String triplet = "" + c + myLetter + otherLetter;
                    String reversedTriplet = "" + otherLetter + myLetter + c;
                    if (commonTriplets.contains(triplet) || commonTriplets.contains(reversedTriplet)) {
                        bonus += 5;
                    }
                }
            }
        }

        return bonus;
    }

    private int calculateVowelBonus(Letter letter) {
        int consonantCount = 0;
        int vowelCount = 0;

        for (Character c : myLetters) {
            if (vowels.contains(c)) {
                vowelCount++;
            } else if (Character.isLetter(c)) {
                consonantCount++;
            }
        }

        // Check if the consonant-to-vowel ratio is greater than 2:1
        if (consonantCount > 2 * vowelCount && vowels.contains(letter.getCharacter())) {
            return 5; // Adjust this value as needed
        }

        return 0;
    }

    private int calculateBidBudget(int playerScore, int remainingRounds) {
        double roundFactor = (double) (totalRounds - currentRound) / totalRounds;
        return (int) (playerScore * (0.1 + (0.2 * roundFactor)));
    }

    @Override
    public String returnWord() {
        List<Character> sortedLetters = new ArrayList<>(myLetters);
        sortedLetters.sort((a, b) -> Integer.compare(ScrabbleValues.letterScore(b), ScrabbleValues.letterScore(a)));

        String bestWord = "";
        int bestScore = 0;

        for (Word word : wordlist) {
            if (canFormWord(word.word, new ArrayList<>(sortedLetters))) {
                int score = ScrabbleValues.getWordScore(word.word);
                score += calculateWordCombinationBonus(word.word);
                if (score > bestScore) {
                    bestScore = score;
                    bestWord = word.word;
                }
            }
        }

        return bestWord;
    }

    private int calculateWordCombinationBonus(String word) {
        int bonus = 0;
        for (int i = 0; i < word.length() - 1; i++) {
            if (commonDoublets.contains(word.substring(i, i+2))) {
                bonus += 2;
            }
            if (i < word.length() - 2 && commonTriplets.contains(word.substring(i, i+3))) {
                bonus += 4;
            }
        }
        return bonus;
    }

    private boolean canFormWord(String word, List<Character> availableLetters) {
        for (char c : word.toCharArray()) {
            if (!availableLetters.remove(Character.valueOf(c))) {
                return false;
            }
        }
        return true;
    }
}