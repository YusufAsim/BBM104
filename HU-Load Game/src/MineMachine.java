import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

/**
 * A concrete class that represents the drill machine in the game.
 */
public class MineMachine {

    private boolean isUpKeyPressed = false;
    private Image image;
    private ImageView view;
    private int money;
    private double fuel;
    private double storage;
    private double fuelConsumptionRate = 0.0004;

    public MineMachine(String image,int x, int y) {
        this.image = new Image(image);
        view = new ImageView(image);
        view.setX(x);
        view.setY(y);
        fuel = 10000;
    }

    /**
     * This method is used to set the fuel consumption rate of the drill machine.
     * @param root the root of the game
     */
    public void consumeFuel(Group root) {
        fuel -= fuelConsumptionRate;
        if (fuel <= 0) {
            // If the fuel reaches 0, apply the endgameFuel scenario
            endGameFuel(root);
        }
    }

    public int getMoney() {
        return money;
    }
    public double getFuel() {
        return fuel;
    }
    private void setFuel(double fuel) {
        this.fuel = fuel;
    }
    public double getStorage() {
        return storage;
    }
    public boolean getIsUpKeyPressed() {
        return this.isUpKeyPressed;
    }
    public void setIsUpKeyPressed(boolean isUpKeyPressed) {
        this.isUpKeyPressed = isUpKeyPressed;
    }
    public ImageView getView() {
        return view;
    }


    /**
     * This method is used to move the drill machine in the game according to the key event.
     * @param event the key event
     * @param blocks the blocks in the game
     * @param root the root of the game
     */
    public void moveTo(KeyEvent event, Block[][] blocks, Group root){
        switch(event.getCode()){
            case UP:
                isUpKeyPressed = true;
                Image imageUp = new Image("assets/drill/drill_23.png");
                view.setImage(imageUp);
                setFuel(fuel - 100);
               if(view.getY() > 100){ // to be sure that the machine is down first row

                   Block UpBlock = blocks[(int) view.getX()/50][(int) (view.getY()-150)/50];
                   if(UpBlock == null){  // if there is no block above the machine
                       view.setY(view.getY()-50);} // move the machine up
               } else if (view.getY() >= 50) {
                     view.setY(view.getY() - 50);
               }
                break;
            case DOWN:
                Block DownBlock = blocks[(int) view.getX()/50][(int) (view.getY()-50)/50];
                setFuel(fuel - 100);
                if (DownBlock.xCoordinate() == view.getX() && DownBlock.yCoordinate() == view.getY() + 50) {
                    Image imageDown = new Image("assets/drill/drill_43.png");
                    view.setImage(imageDown);
                    if (DownBlock instanceof Obstacle) {
                        if (DownBlock instanceof Lava) {
                            endGameLava(root); // end game if lava block is hit
                        }else {
                            setFuel(fuel - 50);
                        }
                    }else{
                        view.setY(view.getY()+50); // move the machine down
                        attributeSetter(blocks);
                        root.getChildren().remove(DownBlock.getImageView()); // remove the block from the root
                        blocks[(int) view.getX()/50][(int) (view.getY()-100)/50] = null;
                    }
                }
                break;
            case LEFT:
                Image imageLeft = new Image("assets/drill/drill_01.png");
                view.setImage(imageLeft);
                setFuel(fuel - 100);
                if(view.getX()-50 >= 0){
                    if(view.getY() >= 100 ){
                        int a = (int) (view.getX()-50)/50;
                        int b = (int) (view.getY()-100)/50;
                        Block LeftBlock = blocks[a][b];
                        if(LeftBlock == null){
                            view.setX(view.getX()-50);
                        }
                        else{ // if there is a block on the left of the machine
                            if(LeftBlock.xCoordinate() == view.getX()-50 && LeftBlock.yCoordinate() == view.getY()){
                                if (LeftBlock instanceof Obstacle) {
                                    if (LeftBlock instanceof Lava) {
                                        endGameLava(root); // end game if lava block is hit
                                    }else {
                                        setFuel(fuel - 50);
                                    }
                                } else {
                                    view.setX(view.getX() - 50);
                                    attributeSetter(blocks);
                                    root.getChildren().remove(LeftBlock.getImageView()); // remove the block from the root
                                    blocks[a][b] = null;
                                }
                            }
                        }
                    }else { // if the machine is in the first row
                        view.setX(view.getX() - 50);
                    }
                }
                break;
            case RIGHT:
                Image imageRight = new Image("assets/drill/drill_56.png");
                view.setImage(imageRight);
                setFuel(fuel - 100);
                if(view.getX() + 50 < 800){
                    if(view.getY() >= 100 ) {
                        int a = (int) (view.getX() + 50) / 50;
                        int b = (int) (view.getY() - 100) / 50;
                        Block RightBlock = blocks[a][b];
                        if (RightBlock == null) {
                            view.setX(view.getX() + 50);
                        } else { // if there is a block on the right of the machine
                            if (RightBlock.xCoordinate() == view.getX() + 50 && RightBlock.yCoordinate() == view.getY()) {
                                if (RightBlock instanceof Obstacle) {
                                    if (RightBlock instanceof Lava) {
                                        endGameLava(root); // end game if lava block is hit
                                    }else {
                                        setFuel(fuel - 50);
                                    }
                                } else {
                                    view.setX(view.getX() + 50);
                                    attributeSetter(blocks);
                                    root.getChildren().remove(RightBlock.getImageView()); // remove the block from the root
                                    blocks[a][b] = null;
                                }
                            }
                        }
                    }else { // if the machine is in the first row
                        view.setX(view.getX() + 50);
                    }
                }
                break;
        }

    }

    /**
     * This method is used to apply gravity to the drill machine in the game.
     * @param blocks the blocks in the game
     * @throws InterruptedException
     */
//    public void gravity(Block[][] blocks) throws InterruptedException {
//        if(view.getY() < 50){ // if the machine is in the first row
//            view.setY(view.getY() + 50);
//        }else{
//            Block DownBlock = blocks[(int) view.getX()/50][(int) (view.getY()-50)/50];
//            if(DownBlock == null){
//                Thread.sleep(300); // making the falling down  smooth
//                view.setY(view.getY() + 50);
//            }
//        }
//    }

    public void gravity(Block[][] blocks) throws InterruptedException {
        int xIndex = (int) view.getX()/50;
        int yIndex = (int) (view.getY()-50)/50;

        if(view.getY() < 50){ // if the machine is in the first row
            view.setY(view.getY() + 50);
        }else if (xIndex >= 0 && xIndex < blocks.length && yIndex >= 0 && yIndex < blocks[0].length){
            Block DownBlock = blocks[xIndex][yIndex];
            if(DownBlock == null){
                Thread.sleep(300); // making the falling down  smooth
                view.setY(view.getY() + 50);
            }
        }
    }


    /**
     * This method is used to set the attributes of the drill machine according to the block it drills.
     * @param blocks the blocks in the game
     */
    private void attributeSetter(Block[][] blocks){
        // the lost fuel of the blocks are approximately proportional to the money values of the blocks
        Block block = blocks[(int) view.getX()/50][(int) (view.getY()-100)/50];
        if(block != null){
            if(block instanceof ValuableEinsteinium){
                ValuableEinsteinium einsteinium = (ValuableEinsteinium) block;
                setFuel(fuel - 200);
                money += einsteinium.getValue();
                storage += einsteinium.getWeight();
            }else if(block instanceof ValuableGoldium){
                ValuableGoldium goldium = (ValuableGoldium) block;
                setFuel(fuel - 70);
                money += goldium.getValue();
                storage += goldium.getWeight();
            }else if(block instanceof ValuableIronium){
                ValuableIronium ironium = (ValuableIronium) block;
                setFuel(fuel - 50);
                money += ironium.getValue();
                storage += ironium.getWeight();
            } else if (block instanceof Soil) {
                setFuel(fuel - 20);
            }
        }
    }


    /**
     * This method is used to end the game when the drill machine hits the lava block.
     * @param root the root of the game
     */
    private void endGameLava(Group root) {
        // creating a red rectangle to cover the screen
        Rectangle gameOverScreen = new Rectangle(800, 750, Color.BROWN);
        root.getChildren().add(gameOverScreen);

        // generating a label to display the game over text on th game over screen
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", 70));
        gameOverLabel.setTextFill(Color.WHITE);
        gameOverLabel.setLayoutX(200);
        gameOverLabel.setLayoutY(300);
        root.getChildren().add(gameOverLabel);
    }


    /**
     * This method is used to end the game when the fuel of the drill machine reaches 0.
     * @param root the root of the game
     */
    private void endGameFuel(Group root) {
        // creating a red rectangle to cover the screen
        Rectangle gameOverScreen = new Rectangle(800, 750, Color.GREEN);
        root.getChildren().add(gameOverScreen);

        // generating a label to display the game over text on th game over screen
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", 70));
        gameOverLabel.setTextFill(Color.WHITE);
        gameOverLabel.setLayoutX(200);
        gameOverLabel.setLayoutY(300);
        root.getChildren().add(gameOverLabel);


        Label collectedMoney = new Label("Collected Money: " + (int)money);
        collectedMoney.setFont(new Font("Arial", 50));
        collectedMoney.setTextFill(Color.WHITE);
        root.getChildren().add(collectedMoney);
        collectedMoney.setLayoutX(150);
        collectedMoney.setLayoutY(400);
    }
}
