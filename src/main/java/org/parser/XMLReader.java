package org.parser;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import java.io.FileInputStream;
import java.util.ArrayList;

import java.math.BigDecimal;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public class XMLReader {
    private Builder tempElement;
    private TagBound bound;
    private ArrayList<TagNode> nodes = new ArrayList<TagNode>();
    private ArrayList<TagAdress> adresses = new ArrayList<TagAdress>();
   // private ArrayList<TagWay> ways = new ArrayList<TagWay>();

  
    public static BigDecimal getAttributeByBigDecimal(XMLStreamReader event, String name) {
        return new BigDecimal(event.getAttributeValue(null, name));
    }
    // @Override
    // public String toString() {
    //     return this.tag.toString();
    // }

    // /**
    //  * The the current tag. Returns null if the tag is empty.
    //  * 
    //  * @return The tag of type HashMap<K,V>, where the types are of {@link ParseTagResult}.
    //  */
    // public HashMap<K, V> getTag() {
    //     return !this.tag.isEmpty() ? tag : null;
    // }

    // public Long getID() {
    //     return (Long) this.getTag().get(Node.ID);
    // }

    // public BigDecimal getLat() {
    //     return (BigDecimal) this.getTag().get(Node.LAT);
    // }

    // public BigDecimal getLon() {
    //     return (BigDecimal) this.getTag().get(Node.LON);
    // }

    // public Class<?> getType() {
    //     return this.tag.getClass();
    // } 

    // // Method to set the tag.
    // public void setTag(HashMap<K,V> tag) {
    //     this.tag = tag;
    // }

    // public boolean isBoundsType() {
    //     return this.getClass().equals(TagBound.class);
    // }

    // public boolean isNodeType() {
    //     return this.getClass().equals(TagNode.class);
    // }

    // public boolean isAdressType() {
    //     return this.getClass().equals(TagAdress.class);
    // }

    // static private String getTagName(XMLStreamReader event) {
    //     return event.getLocalName().intern();
    // }


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
                            tempElement = new Builder(reader, element);
                        };
                        break;
                    case END_ELEMENT:
                        element = reader.getLocalName().intern();
                        switch (element) {
                            case "node":
                                if(!tempElement.getAdressBuilder().isEmpty()){
                                    adresses.add(new TagAdress(tempElement));
                                } else {
                                    nodes.add(new TagNode(tempElement));
                                }
                                break;
                            case "way":
                                break;
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
        System.out.println(adresses);
        //System.out.println(nodes);
        //System.out.println(bound);
        // new XMLParser(this);
    }
    public class Builder {
        private AdressBuilder adressBuilder = new AdressBuilder();
        private BigDecimal id, lat, lon;

        public BigDecimal getID(){
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

        Builder(XMLStreamReader reader, String element){
            switch (element) {
                case "node":
                    this.id = getAttributeByBigDecimal(reader, "id");
                    this.lat = getAttributeByBigDecimal(reader, "lat");
                    this.lon = getAttributeByBigDecimal(reader, "lon");
                    break;
                case "tag":
                    String k = reader.getAttributeValue(null, "k");
                    String v = reader.getAttributeValue(null, "v");

                    parseTag(k, v);
                    break;
                default:
                    break;
            }
        }

        private void parseTag(String k, String v){

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
     * Builder
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

}


