import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A concrete class representing a lava block in the game.
 */
public class Lava extends Obstacle{
    private String name;

    public Lava(String name) {
        this.name = name;
    }

    /**
     * getter method to get the name of the block.
     * @return the name of the block.
     */
    @Override
    public String getName() {
        return name;
    }

    Image image = new Image("assets/underground/lava_01.png"); // getting the image of the lava block.
    ImageView imageView = new ImageView(image); // getting the image view of the lava block.

    /**
     * Method to get the ImageView of the lava block.
     * @return the ImageView of the lava block.
     */
    @Override
    public ImageView getImageView() {
        return imageView;
    }

}
