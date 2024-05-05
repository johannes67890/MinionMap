package util;

import java.util.*;

public enum TransportType {
    
    CAR(Type.getAllCarRoads()),
    FOOT(Type.getAllPedestrianRoads()),
    BIKE(Type.getAllBikeRoads());

    private final List<Type> roadTypes;

    TransportType(List<Type> roadTypes){
        this.roadTypes = roadTypes;
    }

    public List<Type> getRoadTypes(){
        return roadTypes;
    }

    /**
     * A pathfinding value that determines how thorough the search of the right path should.
     * This depends heavily on the speed of the vehicle
     * @param isFastest wether the speed goes into the calculation
     * @return          a value understood by the relax method
     */
    public double getAStarRatio(boolean isFastest){
        if (!isFastest){
            return 1;
        }
        if (this.equals(CAR)){
            return 1180.0;
        }else if (this.equals(FOOT)){
            return 80.0;
        }else if (this.equals(BIKE)){
            return 300.0;
        }else{
            return 100.0;
        }
    }

}
