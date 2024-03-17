package org.parser;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import java.io.FileInputStream;
import java.util.ArrayList;

import java.math.BigDecimal;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public class XMLReader {
    private TagBound bound;
    private ArrayList<TagNode> nodes = new ArrayList<TagNode>();
    private ArrayList<TagAdress> adresses = new ArrayList<TagAdress>();
    private ArrayList<TagWay> ways = new ArrayList<TagWay>();

  
    public static BigDecimal getAttributeByBigDecimal(XMLStreamReader event, String name) {
        return new BigDecimal(event.getAttributeValue(null, name));
    }
    public static Long getAttributeByLong(XMLStreamReader event, String name) {
        return Long.parseUnsignedLong(event.getAttributeValue(null, name));
    }

    private Builder tempBuilder = new Builder();

    public XMLReader(FileDistributer filename) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filename.getFilePath()));
            
            while (reader.hasNext()) {
                reader.next();
                switch (reader.getEventType()) {
                    case START_ELEMENT:
                        String element = reader.getLocalName().intern();
                        if(element.equals("bounds")) {
                            this.bound = new TagBound(reader);
                        }else {
                            tempBuilder.parse(element, reader);
                        };
                        break;
                    case END_ELEMENT:
                        element = reader.getLocalName().intern();
                        switch (element) {
                            case "node":
                                if(!tempBuilder.getAdressBuilder().isEmpty()){
                                    adresses.add(new TagAdress(tempBuilder));
                                } else {
                                    nodes.add(new TagNode(tempBuilder));
                                }
                                tempBuilder = new Builder(); // reset the builder
                                break;
                            case "way":
                                ways.add(new TagWay(tempBuilder));
                                tempBuilder = new Builder(); // reset the builder
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ways.forEach((way) -> {
            if (way.getType() == null) {
                System.out.println("Type is null: " + way);       
            }
        });
        // new XMLParser(this);
    }
    public class Builder {
        private AdressBuilder adressBuilder = new AdressBuilder();
        private WayBuilder wayBuilder = new WayBuilder();

        private String name;
        private Type type;
        private Long id;
        private BigDecimal lat, lon;

        public boolean isEmpty(){
            return this.getAdressBuilder().isEmpty() || this.getWayBuilder().isEmpty() && this.id == null && this.lat == null && this.lon == null;
        }

        public Long getID(){
            return this.id;
        }
        public BigDecimal getLat(){
            return this.lat;
        }
        public BigDecimal getLon(){
            return this.lon;
        }
        public AdressBuilder getAdressBuilder(){
            return this.adressBuilder;
        }
        public WayBuilder getWayBuilder(){
            return this.wayBuilder;
        }
        public String getName(){
            return this.name;
        }
        public Type getType(){
            return this.type;
        }

        private void parse(String element, XMLStreamReader reader){
            switch (element) {
                case "node":
                    this.id = getAttributeByLong(reader, "id");
                    this.lat = getAttributeByBigDecimal(reader, "lat");
                    this.lon = getAttributeByBigDecimal(reader, "lon");
                    break;
                case "way":
                    this.id = getAttributeByLong(reader, "id");                    
                    break;
                case "tag":
                    String k = reader.getAttributeValue(null, "k");
                    String v = reader.getAttributeValue(null, "v");

                    parseTag(k, v);
                    break;
                case "nd":
                    // TODO: figure out if to add the ref to the way or add the node to the way?
                    Long ref = getAttributeByLong(reader, "ref");
                    wayBuilder.addNode(ref);
                    break;
                default:
                    break;
            }
        }

        private void parseTag(String k, String v){
            if(k.equals("name")){
                this.name = v;
            }

            for (Type currType : Type.getTypes()){
                if (k.equals(currType.getKey())){
                    for (String currVal : currType.getValue()) {
                        if (v.equals(currVal) || currVal.equals("")) {
                            this.type = currType;
                            break;
                        }
                    }
                }
            }

            // if the tag is a address tag
            if(k.contains("addr:")){
                switch (k) {
                    case "addr:city":
                    adressBuilder.city(v);
                        break;
                    case "addr:street":
                    adressBuilder.street(v);
                        break;
                    case "addr:housenumber":
                    adressBuilder.house(v);
                        break;
                    case "addr:postcode":
                    adressBuilder.postcode(v);
                        break;
                    case "addr:municipality":
                    adressBuilder.municipality(v);
                        break;
                    default:
                        break;
                }
            }
        }
    
    /**
     * Builder for adress
     */
    public static class AdressBuilder {
        public String street, house, postcode, city, municipality;
        private boolean isEmpty = true;

        public boolean isEmpty() {
            return isEmpty;
        }

        public AdressBuilder street(String _street) {
            street = _street;
            isEmpty = false;
            return this;
        }

        public AdressBuilder house(String _house) {
            house = _house;
            return this;
        }

        public AdressBuilder floor(String _floor) {
            return this;
        }

        public AdressBuilder side(String _side) {
            return this;
        }

        public AdressBuilder postcode(String _postcode) {
            postcode = _postcode;
            return this;
        }

        public AdressBuilder city(String _city) {
            city = _city;
            return this;
        }

        public AdressBuilder municipality(String _municipality) {
            municipality = _municipality;
            return this;
            }
        }
    }
   
    public class WayBuilder {
        private ArrayList<Long> refNodes = new ArrayList<Long>();
        private boolean isEmpty = true;

        public boolean isEmpty() {
            return isEmpty;
        }

        private void addNode(Long ref) {
            if (isEmpty) {
                isEmpty = false;
            }
            refNodes.add(ref);
        }

        public ArrayList<Long> getRefNodes() {
            return refNodes;
        }
    }
}


