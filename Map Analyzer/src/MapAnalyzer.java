import java.util.*;
/**
 * This class is used to analyze a map.
 * It finds the fastest route between two points and the ratio of construction material usage between barely connected and original map.
 */
public class MapAnalyzer {
    /**
     * The main method bodies the components of the prograç.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        String[] lines = FileInput.readFile(args[0], true, true);
        String initialPoint = lines[0].split("\t")[0];
        String finalPoint = lines[0].split("\t")[1];

        ArrayList<Road> roads = createRoadsAndSort(Arrays.copyOfRange(lines, 1, lines.length));// taking the part of the roads

        AnalyzerOfMaps(initialPoint, finalPoint, args[1], roads);
        }

    /**
     * This method analyzes the given map.
     * @param initialPoint The starting point.
     * @param finalPoint  The ending point.
     * @param outputPath The path of the output file.
     * @param roads  The list of roads.
     */
    public static void AnalyzerOfMaps(String initialPoint, String finalPoint, String outputPath, ArrayList<Road> roads){
        float constructionMaterialForOriginalMap = 0;
        float constructionMaterial2ForBarelyConnectedWithOneLane = 0;
        for(Road road : roads){constructionMaterialForOriginalMap+=road.getLength();}

        Map<String, Path> pathMap = new HashMap<>();
        int totalDistance1Int = OutputFormer(originalMap(roads, initialPoint, finalPoint, pathMap), initialPoint, finalPoint, outputPath, "Original");

        List<Road> barelyConnectedMap = barelyConnectedMap(roads, initialPoint, outputPath);
        for(Road road : barelyConnectedMap){constructionMaterial2ForBarelyConnectedWithOneLane+=road.getLength();}
        Map<String, Path> pathMap2 = new HashMap<>();
        for (Road road : barelyConnectedMap) {
            Path path = new Path(road.getInitialPoint(), road.getFinalPoint(), road);
            pathMap2.put(road.getFinalPoint(), path);
        }
        int totalDistance2Int = OutputFormer(pathMap2, initialPoint, finalPoint, outputPath, "Barely Connected");

        FileOutput.writeToFile(outputPath, "Analysis:", true, true);
        FileOutput.writeToFile(outputPath, String.format("Ratio of Construction Material Usage Between Barely Connected and Original Map: %.2f", 2*constructionMaterial2ForBarelyConnectedWithOneLane/constructionMaterialForOriginalMap), true, true);
        FileOutput.writeToFile(outputPath, String.format("Ratio of Fastest Route Between Barely Connected and Original Map: %.2f", (double)totalDistance2Int/totalDistance1Int), true, false);
    }

    /**
     * This method creates roads from the given data and also sorts them.
     * @param roadsData The data of the roads from input file.
     * @return The list of roads.
     */
    public static ArrayList<Road> createRoadsAndSort(String[] roadsData){
        ArrayList<Road> roads = new ArrayList<>();
        for (String roadData : roadsData) {
            String[] roadInfo = roadData.split("\t"); // adding both of two lanes ways
            roads.add(new Road(roadInfo[0], roadInfo[1], Integer.parseInt(roadInfo[2]), Integer.parseInt(roadInfo[3]), true));
            roads.add(new Road(roadInfo[1], roadInfo[0], Integer.parseInt(roadInfo[2]), Integer.parseInt(roadInfo[3]), false));
        }

        Collections.sort(roads, (r1, r2) -> {
            if(r1.getLength() < r2.getLength()){
                return -1; // making r1 come before r2
            }else if(r1.getLength() > r2.getLength()){
                return 1; // making r2 come before r1
            }else{
                if (r1.getId() == r2.getId()) {
                    return 0;
                }
                return Integer.compare(r1.getId(), r2.getId()); // if the lengths are equal, compare the ids
            }
        });
        return roads;
    }


    /**
     * This method forms the output for the given path map.
     * @param pathMap The map of paths.
     * @param initialPoint The starting point.
     * @param finalPoint The ending point.
     * @param outputPath The path of the output file.
     * @param MapType The type of the map.
     * @return The total distance of the fastest route.
     */
    public static int OutputFormer(Map<String, Path> pathMap, String initialPoint, String finalPoint, String outputPath, String MapType){
        ArrayList<String> FastestRoute = new ArrayList<>();
        String totalDistance = fastestRoute(pathMap, initialPoint, finalPoint, FastestRoute, 0);
        int totalDistanceInt = Integer.parseInt(totalDistance);
        if(MapType.equals("Original")){
            FastestRoute.add("Fastest Route from "+ initialPoint +" to "+ finalPoint +" ("+totalDistance+" KM):");
        }else{
            FastestRoute.add("Fastest Route from "+ initialPoint +" to "+ finalPoint +" on Barely Connected Map ("+totalDistance+" KM):");
        }
        Collections.reverse(FastestRoute);// reversing the list to print the route from start to end
        for(String route : FastestRoute){
            FileOutput.writeToFile(outputPath, route, true, true);
        }
        return totalDistanceInt;
    }

    /**
     * This method generates the original map from the given roads.
     * @param roads The list of roads.
     * @param initialPoint The starting point.
     * @param finalPoint The ending point.
     * @param pathMap The map of paths.
     * @return The map of paths of  the original map.
     */
    public static Map<String, Path> originalMap(ArrayList<Road> roads, String initialPoint, String finalPoint, Map<String, Path> pathMap){
        List<Path> pathObjects = new ArrayList<>();
        String referencePoint = initialPoint; // the point that is being checked
        do{
            for(Road road : roads){
                if(road.getInitialPoint().equals(referencePoint)  && !(road.getFinalPoint().equals(initialPoint))){ // if the road is connected to the reference point
                    if(!pathMap.containsKey(road.getFinalPoint())) { // if the road's final point is not in the map
                        Path path = new Path(referencePoint, road.getFinalPoint(), road);
                        pathObjects.add(path);
                    }
                }
            }
            Collections.sort(pathObjects, (p1, p2) -> {
                if(calculateDistanceToStart(p2, pathMap, initialPoint)<calculateDistanceToStart(p1, pathMap, initialPoint)){
                    return 1; // making p2 come before p1
                }else if(calculateDistanceToStart(p2, pathMap, initialPoint)>calculateDistanceToStart(p1, pathMap, initialPoint)){
                    return -1; // making p1 come before p2
                }else{
                    return Integer.compare(p1.getRoad().getId(), p2.getRoad().getId()); // if the distances are equal, compare the ids
                }
            });

            for(Path path : pathObjects){
                if(pathMap.containsKey(path.getFinalPoint()) && pathMap.containsKey(path.getReferencePoint())){
                    continue;
                }
                if(!(pathMap.containsKey(path.getFinalPoint()))){
                    pathMap.put(path.getFinalPoint(), path);
                    referencePoint = path.getFinalPoint(); // changing the reference point to the final point of the path
                    pathObjects.remove(path); // removing the path because of that it is added to the map
                    break;
                }
            }
        }while(!(referencePoint.equals(finalPoint)));// until the reference point becomes the final point
        return pathMap;
    }


    /**
     * This method is used to generate a barely connected map from the given roads.
     * @param roads The list of roads.
     * @param initialPoint The starting point.
     * @param outputPath The path of the output file.
     * @return The list of roads of a barely connected map.
     */
    public static List<Road> barelyConnectedMap(ArrayList<Road> roads, String initialPoint, String outputPath) {
        ArrayList<Road> roadList = new ArrayList<>();
        List<Road> barelyConnectedMap = new ArrayList<>();
        Set<String> pointsInMap = new HashSet<>(); // we prefer to use a set to avoid duplicate points
        ArrayList<String> allPoints = new ArrayList<>();

        // Adding all points to the allPoints list
        for (Road road : roads) {
            allPoints.add(road.getInitialPoint());
        }

        Collections.sort(allPoints); // Sort the allPoints list alphabetically

        // Adding all roads connected to the initial point
        for (Road road : roads) {
            if (road.getInitialPoint().equals(initialPoint)) {
                roadList.add(road);
            }
        }

        pointsInMap.add(initialPoint);

        while (!roadList.isEmpty() && barelyConnectedMap.size() < allPoints.size() - 1) { // until the road list is empty or all points are connected
            // Sort the road list
            Collections.sort(roadList, (r1, r2) -> {
                if (r1.getLength() < r2.getLength()) {
                    return -1;
                } else if (r1.getLength() > r2.getLength()) {
                    return 1;
                } else {
                    return Integer.compare(r1.getId(), r2.getId()); // if the lengths are equal, compare the ids
                }
            });

            Road selectedRoad = roadList.get(0); // getting first element of sorted list.
            roadList.remove(0);

            // controlling if adding the road connects to a new point
            if (pointsInMap.contains(selectedRoad.getInitialPoint()) && !pointsInMap.contains(selectedRoad.getFinalPoint())) {

                barelyConnectedMap.add(selectedRoad); // adding the selected road to the barely connected map
                pointsInMap.add(selectedRoad.getFinalPoint());

                for (Road road : roads) { // adding the roads connected to the final point of the selected road
                    if (road.getInitialPoint().equals(selectedRoad.getFinalPoint()) && !pointsInMap.contains(road.getFinalPoint())) {
                        roadList.add(road);
                    }
                }
            }
        }
        // after the loop, sorting the barely connected map
        Collections.sort(barelyConnectedMap, (r1, r2) -> {
            if(r1.getLength() < r2.getLength()){
                return -1;
            }else if(r1.getLength() > r2.getLength()){
                return 1;
            }else{
                return Integer.compare(r1.getId(), r2.getId()); // if the lengths are equal, comparing the ids
            }
        });

        // writing  the barely connected map to the output file
        FileOutput.writeToFile(outputPath,"Roads of Barely Connected Map is:", true, true);
        for (Road road : barelyConnectedMap) {
            if(road.isOnSameDirectionWithInput()){
                FileOutput.writeToFile(outputPath, road.getInitialPoint() + "\t" + road.getFinalPoint() + "\t" + road.getLength() + "\t" + road.getId(), true, true);
            }else{
                FileOutput.writeToFile(outputPath, road.getFinalPoint() + "\t" + road.getInitialPoint() + "\t" + road.getLength() + "\t" + road.getId(), true, true);
            }
        }
        return barelyConnectedMap;
    }

    /**
     * This method calculates the fastest route from the start point to the end point.
     * @param pathMap The map of paths.
     * @param start The starting point.
     * @param end The ending point.
     * @param FastestRoute The list to store the fastest route.
     * @param totalDistance The total distance of the fastest route.
     * @return The total distance of the fastest route.
     */
    public static String fastestRoute(Map<String, Path> pathMap, String start, String end, ArrayList<String> FastestRoute, int totalDistance){
        if(pathMap.get(end).getReferencePoint().equals(start)){ // if the end point is the starting point
            FastestRoute.add(creatRouteLine(pathMap, end)); // adding the route line to the list
            totalDistance+=pathMap.get(end).getRoad().getLength(); // adding the length of the road to the total distance
            return Integer.toString(totalDistance);
        }
        FastestRoute.add(creatRouteLine(pathMap, end));
        totalDistance+=pathMap.get(end).getRoad().getLength();
        return fastestRoute(pathMap, start, pathMap.get(end).getReferencePoint(), FastestRoute, totalDistance);
    }

    /**
     * This method calculates the distance from the start point to the given path.
     * @param path The path to calculate the distance to.
     * @param pathMap The map of paths.
     * @param start The starting point.
     * @return The distance from the start point to the given path.
     */
    public static int calculateDistanceToStart(Path path, Map<String, Path> pathMap, String start){
        int Distance = path.getRoad().getLength();
        String pointTrace = path.getReferencePoint(); // showing the which point is being checked
        while(!(pointTrace.equals(start))){// until the pointTrace becomes the starting point
            for(String key : pathMap.keySet()){
                if(pointTrace.equals(key)){// if the pointTrace point is a visited point before
                    Distance+=pathMap.get(key).getRoad().getLength();
                    pointTrace = pathMap.get(key).getReferencePoint(); // changing the pointTrace to the reference point of the path
                }
            }
        }
        path.setDistance(Distance); // also setting the distance of the path
        return Distance;
    }

    /**
     * This method creates a route line for the given end point.
     * @param pathMap The map of paths.
     * @param end The ending point.
     * @return The route line for the given end point.
     */
    public static String creatRouteLine(Map<String, Path> pathMap, String end){
        String output;
        if(pathMap.get(end).getRoad().isOnSameDirectionWithInput()){ // if the road is on the same direction with the input
            output = pathMap.get(end).getReferencePoint()+ "\t" + end + "\t" +pathMap.get(end).getRoad().getLength() + "\t"  + pathMap.get(end).getRoad().getId();
        }else{ // otherwise
            output = end+ "\t" + pathMap.get(end).getReferencePoint() + "\t" +pathMap.get(end).getRoad().getLength() + "\t"  + pathMap.get(end).getRoad().getId();
        }
        return output;
    }
}