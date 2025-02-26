import javafx.scene.image.ImageView;


/**
 * Abstract class representing all blocks in the game.
 */
public abstract class Block extends ImageView {
   public String name;

    /**
     * abstract method to get the ImageView of the block.
     */
   public abstract ImageView getImageView();

   public Block(String name) {
       this.name = name;
   }

    /**
     * getter for the name of the block.
     * @return name of the block.
     */
   public String getName(){
        return this.name;
   }

   /**
    * Returns the x-coordinate of the block.
    * @return x-coordinate of the block.
    */
   public int xCoordinate(){
        return (int) this.getImageView().getX();
   }

    /**
     * Returns the y-coordinate of the block.
     * @return y-coordinate of the block.
     */
   public int yCoordinate(){
        return (int) this.getImageView().getY();
   }
}