public class Player {
    private String name;
    private int score = 0;
    private boolean isActivePlayer = true;

    public boolean isActivePlayer() {
        return isActivePlayer;
    }//getter
    public  String getName(){
        return name;
    }//getter
    public int getScore() {
        return score;
    }//getter

    public void setActivePlayer(boolean activePlayer) {
        isActivePlayer = activePlayer;
    }//setter


    public Player(String name) {
        this.name = name;
    }// constructor with only name var

    /**
     * This method returns the current score attribute of the player object according to dice numbers.
     * @param diceNumbers the numbers of dices thrown in the game in type of String.
     * @return the current amount of score of player.
     */
    public int scoreCalculator(String diceNumbers) {

        String[] stringArray = diceNumbers.split("-");
        int firstNumber = Integer.parseInt(stringArray[0]);
        int secondNumber = Integer.parseInt(stringArray[1]);


        if (firstNumber == 1 && secondNumber == 1) {// in the case that player's game over
            return 0;
        } else if (firstNumber == 1 || secondNumber == 1) {// in the case that player's score is not changed
            return score;
        } else if (firstNumber == 0 && secondNumber == 0) {// in the case skipping the turn
            return score;
        }else {
            score = score + firstNumber + secondNumber;
            return score;
        }
    }


}

