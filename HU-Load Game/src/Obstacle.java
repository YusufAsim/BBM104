import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A concrete class representing an obstacle block in the game.
 */
public class Obstacle extends Block{
    public Obstacle() {
        super("Obstacle");
    }


    Image image = new Image("assets/underground/obstacle_01.png");// getting the image of the obstacle block.
    ImageView imageView = new ImageView(image);// getting the image view of the obstacle block.
    /**
     * Method to get the ImageView of the obstacle block.
     * @return the ImageView of the obstacle block.
     */
    @Override
    public ImageView getImageView() {
        return imageView;
    }
}

