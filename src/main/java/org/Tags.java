package org;

public enum Tags {
    BOUNDS, NODE, WAY, TAG;
    
    public enum Node {
        ID, LAT, LON;
    }

    public enum Adress {
        ID, LAT, LON, CITY, STREET, HOUSENUMBER, POSTCODE, COUNTRY
    }

    static public Adress convert(Node node) {
        switch (node) {
            case ID:
                return Adress.ID;
            case LAT:
                return Adress.LAT;
            case LON:
                return Adress.LON;
            default:
                throw new IllegalArgumentException("Invalid node: " + node);
        }
    }

    static public Adress convert(String value) {
        switch (value) {
            case "addr:city":
                return Adress.CITY;
            case "addr:street":
                return Adress.STREET;
            case "addr:housenumber":
                return Adress.HOUSENUMBER;
            case "addr:postcode":
                return Adress.POSTCODE;
            case "addr:country":
                return Adress.COUNTRY;
            default:
                return null;
        }
    }
    
    public enum Bounds {
        MINLAT, MAXLAT, MINLON, MAXLON
    }
}
