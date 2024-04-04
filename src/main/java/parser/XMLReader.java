package parser;
import static javax.xml.stream.XMLStreamConstants.*;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

/**
 * Reader for a OSM XML file.
 * <p>
 * Uses the {@link XMLStreamReader} to read the file and parse the data into the different classes: {@link TagNode}, {@link TagNode} , {@link TagAddress} and {@link TagWay}.
 * </p>
 * 
 */
public class XMLReader {
    private TagBound bound;
    private HashMap<Long, TagNode> nodes = new HashMap<Long, TagNode>();
    private HashMap<Long, TagAddress> addresses = new HashMap<Long, TagAddress>();
    private HashMap<Long, TagRelation> relations = new HashMap<Long, TagRelation>();
    private HashMap<Long, TagWay> ways = new HashMap<Long, TagWay>();

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

    /**
     * Get a node by its id.
     * <p>
     * Returns null if the node is not found.
     * </p>
     * @param id - The id of the node to get.
     * @return The node with the id.
     */
    public TagNode getNodeById(Long id){
        return nodes.get(id);
    }

    public TagWay getWayById(Long id){
        return ways.get(id);
    }

    public TagAddress getAddressById(Long id){
        return addresses.get(id);
    }

    public TagRelation getRelationById(Long id){
        return relations.get(id);
    }
    
    public HashMap<Long, TagNode> getNodesMap(){
        return nodes;
    }

    public ArrayList<TagNode> getNodes(){
        ArrayList<TagNode> nodesList = new ArrayList<>();
        
        for(TagNode node : nodes.values()){
            nodesList.add(node);
        }
        return nodesList;
    }

    public ArrayList<TagWay> getWays(){
        ArrayList<TagWay> waysList = new ArrayList<>();
        
        for(TagWay way : ways.values()){
            waysList.add(way);
        }
        return waysList;
    }

    public ArrayList<TagAddress> getAddresses(){
        ArrayList<TagAddress> addressesList = new ArrayList<>();
        
        for(TagAddress address : addresses.values()){
            addressesList.add(address);
        }
        return addressesList;
    }
    
    public ArrayList<TagRelation> getRelations(){
        ArrayList<TagRelation> relations = new ArrayList<>();
        
        for(TagRelation relation : relations){
            relations.add(relation);
        }
        return relations;
    }


    public TagBound getBound(){
        return bound;
    }

    private Builder tempBuilder = new Builder();

    public XMLReader(String filepath) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filepath));
            
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
                                    addresses.put(tempBuilder.getId(), new TagAddress(tempBuilder));
                                } else {
                                    nodes.put(tempBuilder.getId(), new TagNode(tempBuilder));
                                }
                                tempBuilder = new Builder(); // reset the builder
                                break;
                            case "way":
                                ways.put(tempBuilder.getId(), new TagWay(tempBuilder));
                                tempBuilder = new Builder(); // reset the builder
                            case "relation":
                                relations.put(tempBuilder.getId(), new TagRelation(tempBuilder));
                                tempBuilder = new Builder(); // reset the builder
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                    }
            }
            System.out.println(relations.get(10343794L).toString());
            System.out.println("Relations " + relations.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        private RelationBuilder relationBuilder = new RelationBuilder();

        private String name; // name from a <tag> in a parrent element
        private Type type;
        private Long id;
        private BigDecimal lat, lon;

        public boolean isEmpty(){
            return this.getAddressBuilder().isEmpty() || this.getWayBuilder().isEmpty() || this.getRelationBuilder().isEmpty()
             && this.id == null && this.lat == null && this.lon == null;
        }

        public Long getId(){
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

        public RelationBuilder getRelationBuilder(){
            return this.relationBuilder;
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
                case "relation":
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
                case "member":
                    relationBuilder.parseMember(reader);
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

            // TODO: add the type to the builder
            // if(!this.getRelationBuilder().isEmpty()){
            //     for (Type currType : Type.getTypes()){
            //         if (k.equals(currType.getKey())){
            //             for (String currVal : currType.getValue()) {
            //                 if (v.equals(currVal) || currVal.equals("")) {
            //                     this.type = currType;
            //                     break;
            //                 }
            //             }
            //         }
            //     }
            // }

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

    public class RelationBuilder {
        private boolean isEmpty;
        public TagRelation relation;

        RelationBuilder() {
            this.relation = new TagRelation();
            this.isEmpty = true;
        }

        public boolean isEmpty() {
            return isEmpty;
        }


        public TagRelation getRelation() {
            return relation;
        }
       

        public void parseMember(XMLStreamReader reader) {
            switch (reader.getAttributeValue(null, "type")) {
                case "node":
                    relation.addNode(getNodeById(getAttributeByLong(reader, "ref")));
                    break;
                case "way":
                    long ref = getAttributeByLong(reader, "ref");
                    if(getWayById(ref) != null){
                        switch (reader.getAttributeValue(null, "role")) {
                            case "outer":
                                relation.addOuter(getWayById(ref));
                            case "inner":
                                relation.addInner(getWayById(ref));
                                break;
                            default:
                                relation.addWay(getWayById(ref));
                                break;
                        }
                    }
                    break;
                case "relation":
                    relation.addRelation(getRelationById(getAttributeByLong(reader, "ref")));
                    break;
                default:
                    break;
            }
        }
        
    }
}


