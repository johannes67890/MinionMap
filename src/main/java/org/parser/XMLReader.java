package org.parser;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import java.io.FileInputStream;
import java.util.ArrayList;

import java.math.BigDecimal;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Reader for a OSM XML file.
 * <p>
 * Uses the {@link XMLStreamReader} to read the file and parse the data into the different classes: {@link TagNode}, {@link TagNode} , {@link TagAddress} and {@link TagWay}.
 * </p>
 * 
 */
public class XMLReader {
    private TagBound bound;
    private ArrayList<TagNode> nodes = new ArrayList<TagNode>();
    private ArrayList<TagAddress> addresses = new ArrayList<TagAddress>();
    private ArrayList<TagWay> ways = new ArrayList<TagWay>();


    public TagBound getBound() {
        return bound;
    }

    public ArrayList<TagNode> getNodes() {
        return nodes;
    }

    public ArrayList<TagAddress> getAddresses() {
        return addresses;
    }

    public ArrayList<TagWay> getWays() {
        return ways;
    }
    

    /**
     * Get a attrubute from the {@link XMLStreamReader} as a {@link BigDecimal}.
     * @param event - The {@link XMLStreamReader} to get the attribute from.
     * @param name - The name of the attribute to get. ({@link String})
     * @return The attribute as a {@link BigDecimal}.
     */
    public static BigDecimal getAttributeByBigDecimal(XMLStreamReader event, String name) {
        return new BigDecimal(event.getAttributeValue(null, name));
    }
    /**
     * Get a attrubute from the {@link XMLStreamReader} as a {@link Long}.
     * @param event - The {@link XMLStreamReader} to get the attribute from.
     * @param name - The name of the attribute to get. ({@link String})
     * @return The attribute as a {@link Long}.
     */
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
                                if(!tempBuilder.getAddressBuilder().isEmpty()){
                                    addresses.add(new TagAddress(tempBuilder));
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

    /**
     * Builder for a single XML element.
     * <p>
     * The builder contains a {@link AdressBuilder} and a {@link WayBuilder} to construct a {@link TagAddress} or a {@link TagWay}.
     * </p>
     */
    public class Builder {
        private AddressBuilder addressBuilder = new AddressBuilder();
        private WayBuilder wayBuilder = new WayBuilder();

        private String name; // name from a <tag> in a parrent element
        private Type type;
        private Long id;
        private BigDecimal lat, lon;

        public boolean isEmpty(){
            return this.getAddressBuilder().isEmpty() || this.getWayBuilder().isEmpty() && this.id == null && this.lat == null && this.lon == null;
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
        public AddressBuilder getAddressBuilder(){
            return this.addressBuilder;
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

        /**
         * Parse the XML element and add the data to the builder(s).
         * @param element - The name of the element to parse.
         * @param reader - The {@link XMLStreamReader} to get the data from.
         */
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

        /**
         * Parse a tag and add the data to the builder.
         * @param k - The key of the tag.
         * @param v - The value of the tag.
         */
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
                    addressBuilder.city(v);
                        break;
                    case "addr:street":
                    addressBuilder.street(v);
                        break;
                    case "addr:housenumber":
                    addressBuilder.house(v);
                        break;
                    case "addr:postcode":
                    addressBuilder.postcode(v);
                        break;
                    case "addr:municipality":
                    addressBuilder.municipality(v);
                        break;
                    default:
                        break;
                }
            }
        }
    
    /**
     * Builder for a single address.
     * <p>
     * Constructs a instance of the builder, that later can be used to construct a {@link TagAddress}.
     * </p>
     */
    public static class AddressBuilder {
        public String street, house, postcode, city, municipality;
        private boolean isEmpty = true;

        public boolean isEmpty() {
            return isEmpty;
        }

        public AddressBuilder street(String _street) {
            street = _street;
            isEmpty = false;
            return this;
        }

        public AddressBuilder house(String _house) {
            house = _house;
            return this;
        }

        public AddressBuilder floor(String _floor) {
            return this;
        }

        public AddressBuilder side(String _side) {
            return this;
        }

        public AddressBuilder postcode(String _postcode) {
            postcode = _postcode;
            return this;
        }

        public AddressBuilder city(String _city) {
            city = _city;
            return this;
        }

        public AddressBuilder municipality(String _municipality) {
            municipality = _municipality;
            return this;
            }
        }
    }
   /**
    * Builder for a single way.
    * <p>
    * Constructs a instance of the builder, that later can be used to construct a {@link TagWay}.
    * </p>
    */
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


