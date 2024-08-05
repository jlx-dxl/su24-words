package words.g6;

import words.core.*;

import java.util.*;

public class g6PlayerV1 extends Player{


   public String returnHypoWord(List<Character> myLettersHypothetical){

       // verbose way of building a String from a bunch of characters.
       char c[] = new char[myLettersHypothetical.size()];
       for (int i = 0; i < c.length; i++) {
           c[i] = myLettersHypothetical.get(i);
       }
       String s = new String(c);

       // iterate through our word list. If we find one we can build,
       // check to see if it's an improvement on the best one we've seen.
       Word ourletters = new Word(s);
       Word bestword = new Word("");
       for (Word w : wordlist) {
           if (ourletters.contains(w)) {
               if (w.score > bestword.score) {
                   bestword = w;
               }

           }
       }

       return bestword.word;

   }



    public int bid(Letter bidLetter, List<PlayerBids> playerBidList,
                   int totalRounds, ArrayList<String> playerList,
                   SecretState secretstate, int playerID) {

        List<Character> myLettersHypothetical = new ArrayList<>(this.myLetters);

        int round = 0;

        char currentbidLetter = bidLetter.getCharacter();
        int bidLetterValue = bidLetter.getValue();
        int myBid = 0;

        myLettersHypothetical.add(currentbidLetter);
        String endingWordHypo = returnHypoWord(myLettersHypothetical);
        String endingWord = returnWord();

        int scoreHypo = ScrabbleValues.getWordScore(endingWordHypo);
        int scoreNow = ScrabbleValues.getWordScore(endingWord);

        //System.err.println("scoreHypo : " +  scoreHypo);
        //System.err.println("scoreNow :" + scoreNow);



        if (scoreHypo >= scoreNow) {
            myBid = 3;
            //myBid = numPlayers;
        }


        //myBid = Math.min(scoreNow, (scoreHypo-scoreNow));
        return myBid;
        }
    }



