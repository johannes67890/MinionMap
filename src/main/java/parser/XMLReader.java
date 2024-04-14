package parser;

import static javax.xml.stream.XMLStreamConstants.*;

import java.io.FileInputStream;
import java.util.HashMap;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.FileNotFoundException;

/**
 * Reader for a OSM XML file.
 * <p>
 * Uses the {@link XMLStreamReader} to read the file and parse the data into the different classes: {@link TagNode}, {@link TagNode} , {@link TagAddress} and {@link TagWay}.
 * </p>
 */
public class XMLReader {
    private static TagBound bound;
    private static HashMap<Long, TagNode> nodes = new HashMap<Long, TagNode>();
    private static HashMap<Long, TagAddress> addresses = new HashMap<Long, TagAddress>();
    private static HashMap<Long, TagRelation> relations = new HashMap<Long, TagRelation>();
    private static HashMap<Long, TagWay> ways = new HashMap<Long, TagWay>();

    /**
     * Get the {@link TagBound} of the XML file.
     * @return The {@link TagBound} of the XML file.
     */
    public static TagBound getBound(){
        return bound;
    }

    /**
     * Get a {@link TagNode} by its id.
     * <p>
     * Returns null if the node is not found.
     * </p>
     * @param id - The {@link Node#ID} of the node to get.
     * @return The {@link TagNode} with the id.
     */
    public static TagNode getNodeById(Long id){
        return nodes.get(id);
    }

    /**
     * Get a {@link TagWay} by its id.
     * <p>
     * Returns null if the way is not found.
     * </p>
     * @param id - The {@link Way#ID} of the way to get.
     * @return The {@link TagWay} with the id.
     */
    public static TagWay getWayById(Long id){
        return ways.get(id);
    }

    /**
     * Get a {@link TagAddress} by its id.
     * <p>
     * Returns null if the address is not found.
     * </p>
     * @param id - The {@link Address#ID} of the address to get.
     * @return The {@link TagAddress} with the id.
     */
    public static TagAddress getAddressById(Long id){
        return addresses.get(id);
    }

    /**
     * Get a {@link TagRelation} by its id.
     * <p>
     * Returns null if the relation is not found.
     * </p>
     * @param id - The {@link Relation#ID} of the relation to get.
     * @return The {@link TagRelation} with the id.
     */
    public static TagRelation getRelationById(Long id){
        return relations.get(id);
    }

    /**
     * Get all the {@link TagNode}s in the XML file.
     * @return A {@link HashMap} of the as {@link Node#ID} to all the {@link TagNode}s in the XML file.
     */
    public static HashMap<Long, TagNode> getNodes(){
        return nodes;
    }

    /**
     * Get all the {@link TagAddress}' in the XML file.
     * @return A {@link HashMap} of the keys as {@link Address#ID} to all the {@link TagAddress}s in the XML file.
     */
    public static HashMap<Long, TagAddress> getAddresses(){
        return addresses;
    }

    /**
     * Get all the {@link TagRelation}s in the XML file.
     * @return A {@link HashMap} of the keys as {@link Relation#ID} to all the {@link TagRelation}s in the XML file.
     */
    public static HashMap<Long, TagRelation> getRelations(){
        return relations;
    }

    /**
     * Get all the {@link TagWay}s in the XML file.
     * @return A {@link HashMap} of the keys as {@link Way#ID} to all the {@link TagWay}s in the XML file.
     */
    public static HashMap<Long, TagWay> getWays(){
        return ways;
    }

    private XMLBuilder tempBuilder = new XMLBuilder();
    
    /**
     * Parses the XML File for a OSM file.
     * <p>
     * Uses the {@link XMLStreamReader} to read the file and parse the data into the different classes: {@link TagBound}, {@link TagNode}, {@link TagNode} , {@link TagAddress} and {@link TagWay}.
     * </p>
     * 
     * @param filepath - The path to the XML file.
     */
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
                            bound = new TagBound(reader);
                            new XMLWriter(bound);
                        }else {
                            tempBuilder.parse(element, reader);
                        };
                       
                        break;
                    case END_ELEMENT:
                        element = reader.getLocalName().intern();
                        switch (element) {
                            case "node":
                                if(!tempBuilder.getAddressBuilder().isEmpty()){
                                    XMLWriter.appendToBinary(new TagAddress(tempBuilder));
                                    addresses.put(tempBuilder.getId(), new TagAddress(tempBuilder));
                                } else {
                                    XMLWriter.appendToBinary(new TagNode(tempBuilder));
                                    nodes.put(tempBuilder.getId(), new TagNode(tempBuilder));
                                }
                                tempBuilder = new XMLBuilder(); // Reset the builder
                                break;
                            case "way":
                                XMLWriter.appendToBinary(new TagWay(tempBuilder));
                                ways.put(tempBuilder.getId(), new TagWay(tempBuilder));
                                tempBuilder = new XMLBuilder();
                            case "relation":
                                XMLWriter.appendToBinary(new TagRelation(tempBuilder));
                                relations.put(tempBuilder.getId(), new TagRelation(tempBuilder));
                                tempBuilder = new XMLBuilder();
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                    }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}