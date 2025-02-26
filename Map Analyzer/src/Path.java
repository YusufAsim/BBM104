/**
 * This class represents a path between two points in the map.
 * It contains the reference point, the final point, the road that connects them and the distance between them.
 */
public class Path {
    private int distance;
    private final String referencePoint;
    private final String finalPoint;

    private final Road road;

    public Path(String referencePoint, String finalPoint, Road road){
        this.referencePoint = referencePoint;
        this.finalPoint = finalPoint;
        this.road = road;
        this.distance = 0;
    }


    public String getReferencePoint(){
        return referencePoint;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public String getFinalPoint(){
        return finalPoint;
    }

    public Road getRoad(){
        return road;
    }
}