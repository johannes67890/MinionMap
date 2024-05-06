package util;

import java.util.*;

/**
 * This is a grouping of types used for pathfinding. 
 * It is grouped into 3 groups:
 * - CAR
 * - BIKE
 * - FOOT
 * 
 * These have grouped specific types from the class {@link Type}
 */
public enum TransportType {
    
    CAR(Type.getAllCarRoads()),
    FOOT(Type.getAllPedestrianRoads()),
    BIKE(Type.getAllBikeRoads());

    private final List<Type> roadTypes;

    TransportType(List<Type> roadTypes){
        this.roadTypes = roadTypes;
    }

    /**
     * Returns the assigned {@link Type}s to this {@link TransportType}
     * @return the assigned {@link Type}s to this {@link TransportType}
     */
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
