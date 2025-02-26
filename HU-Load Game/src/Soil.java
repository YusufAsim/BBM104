import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A concrete class representing a soil block in the game.
 */
public class Soil extends Block {
    public Soil() {
        super("Soil");
    }

    Image image = new Image("assets/underground/soil_01.png"); // getting the image of the soil block.
    ImageView imageView = new ImageView(image); // getting the image view of the soil block.


    /**
     * Method to get the ImageView of the soil block.
     * @return the ImageView of the soil block.
     */
    public ImageView getImageView() {
        return imageView;
    }
}
