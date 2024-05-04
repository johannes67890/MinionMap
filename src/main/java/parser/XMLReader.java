package parser;

import static javax.xml.stream.XMLStreamConstants.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import gnu.trove.map.hash.TLongObjectHashMap;
import parser.chunking.XMLWriter;
import structures.Trie;

/**
 * XMLReader for reading the OSM XML file.
 * <p>
 * The XMLReader reads the XML file and parses the data into the different classes: {@link TagBound}, {@link TagNode}, {@link TagAddress} and {@link TagWay}.
 * </p>
 * 
 */
public class XMLReader {
    private static TagBound bound;
    private static TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<TagNode>();
    //TODO: Possibly remove addresses?
    private static TLongObjectHashMap<TagAddress> addresses = new TLongObjectHashMap<TagAddress>();
    private static TLongObjectHashMap<TagRelation> relations = new TLongObjectHashMap<TagRelation>();
    private static TLongObjectHashMap<TagWay> ways = new TLongObjectHashMap<TagWay>();
    private static Trie trie = new Trie();

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
    public static TLongObjectHashMap<TagNode> getNodes(){
        return nodes;
    }

    /**
     * Get all the {@link TagAddress}' in the XML file.
     * @return A {@link HashMap} of the keys as {@link Address#ID} to all the {@link TagAddress}s in the XML file.
     */
    public static TLongObjectHashMap<TagAddress> getAddresses(){
        return addresses;
    }

    /**
     * Get all the {@link TagRelation}s in the XML file.
     * @return A {@link HashMap} of the keys as {@link Relation#ID} to all the {@link TagRelation}s in the XML file.
     */
    public static TLongObjectHashMap<TagRelation> getRelations(){
        return relations;
    }

    /**
     * Get all the {@link TagWay}s in the XML file.
     * @return A {@link HashMap} of the keys as {@link Way#ID} to all the {@link TagWay}s in the XML file.
     */
    public static TLongObjectHashMap<TagWay> getWays(){
        return ways;
    }

    public static Trie getTrie(){
        return trie;
    }

    private XMLBuilder tempBuilder = new XMLBuilder();
    
    /**
     * Parses the XML File for a OSM file.
     * <p>
     * Uses the {@link XMLStreamReader} to read the file and parse the data into the different classes: {@link TagBound}, {@link TagNode}, {@link TagNode} , {@link TagAddress} and {@link TagWay}.
     * </p>
     * Each element is read and the data is parsed into the diffrent "Tag-builders" and then parsed into the correct class.
     * @see XMLBuilder
     * @param filepath - The path to the XML file.
     */
    public XMLReader(String filepath) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(filepath));
            // start timer
            long start = System.currentTimeMillis();
            while (reader.hasNext()) {                
                reader.next();
                switch (reader.getEventType()) {
                    case START_ELEMENT:
                        String element = reader.getLocalName().intern();
                        if(element.equals("bounds")) {
                            bound = MecatorProjection.project(new TagBound(reader));
                        }else {
                            tempBuilder.parse(element, reader);
                        };
                        break;
                    case END_ELEMENT:
                        element = reader.getLocalName().intern();
                        switch (element) {
                            case "node":
                                if(!tempBuilder.getAddressBuilder().isEmpty()){
                                    TagAddress address = new TagAddress(tempBuilder);
                                    addresses.put(tempBuilder.getId(), address);
                                    trie.insert(address);
                                } else {
                                    nodes.put(tempBuilder.getId(), new TagNode(tempBuilder));
                                }
                                tempBuilder = new XMLBuilder(); // Reset the builder
                                break;
                            case "way":
                                TagWay way = new TagWay(tempBuilder);
                        
                                ways.put(tempBuilder.getId(), way);
                                tempBuilder = new XMLBuilder();
                                break;
                            case "relation":
                                TagRelation relation = new TagRelation(tempBuilder);
                                relations.put(tempBuilder.getId(), relation);
                                
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
            nodes = null;  
            reader.close();
      
            // end timer
            long end = System.currentTimeMillis();
            System.out.println("Time total: " + (end - start) + "ms");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void clearTags(){
        nodes = null;
        ways = null;
        relations = null;
    }
}
