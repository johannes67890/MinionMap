package parser;

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

}
