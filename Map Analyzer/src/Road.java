/**
 * Road class represents a road in the city. It has an initial point, a final point, a length, an id and a boolean that indicates if the road is on the same direction with the input.
 */
public class Road{
    private final String initialPoint;
    private final String finalPoint;
    private final int length;
    private final int id;

    private final boolean isOnSameDirectionWithInput; // true if the road is on the same direction with the input

    public Road(String initialPoint, String finalPoint, int lentgh, int id, boolean isOnSameDirectionWithInput){
        this.initialPoint = initialPoint;
        this.finalPoint = finalPoint;
        this.length = lentgh;
        this.id = id;
        this.isOnSameDirectionWithInput = isOnSameDirectionWithInput;
    }

    public int getLength(){
        return length;
    }
    public String getFinalPoint(){
        return finalPoint;
    }

    public String getInitialPoint(){
        return initialPoint;
    }

    public int getId(){
        return id;
    }

    public boolean isOnSameDirectionWithInput(){
        return isOnSameDirectionWithInput;
    }
}
