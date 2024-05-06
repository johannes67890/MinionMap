package parser;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

import gnu.trove.map.hash.TLongObjectHashMap;
import structures.Trie;
import structures.KDTree.Tree;

/**
 * The Model class is responsible for initializing the model values
 * It is a singleton class that initializes the model values from the input stream by utilizing the XMLReader class to parse all needed values from the input stream
 * Needed values are the bound, trie, nodes and addresses for the model
 * The Model class initializes the tree by calling the Tree class and adding the ways, relations and addresses to the tree
 * The Model class is serializable
 * The Model class has a private constructor
 * The Model class has a static instance of the Model class
 * 
 */
public class Model implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private TagBound bound;
    private TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<TagNode>();
    private TLongObjectHashMap<TagAddress> addresses = new TLongObjectHashMap<TagAddress>();
    private Trie trie = new Trie();

    private static Model instanceModel; // Singleton instance of the Model class


    /**
     * Returns the instance of the Model class
     * @return the instance of the Model class
     */
    public static Model getInstanceModel(){
        if(instanceModel == null) {
            instanceModel = new Model();
        }

        return instanceModel;
    }

    /**
     * Updates the model values
     * @param is the input stream
     * @return the updated instance of the Model class
     */
    public static Model updateModelValues(InputStream is){
        return getInstanceModel().setModelValues(is);
    }

    /**
     * Sets the model values from the input stream
     * @param is the input stream
     * @return the instance of the Model class
     */
    private Model setModelValues(InputStream is){
        new XMLReader(is);
        bound = XMLReader.getBound();
        trie = XMLReader.getTrie();
        nodes = XMLReader.getNodes();
        addresses = XMLReader.getAddresses();
        XMLReader.clearTags();
        System.gc();

        System.out.println("Initializing tree: ");
        long startTree = System.currentTimeMillis();
        initializeTrees();
        long stopTree = System.currentTimeMillis();
        System.out.println("Time to tree - total: " + (stopTree - startTree) + "ms");

        return getInstanceModel();
    }

    /**
     * Private constructor for the Model class
     * Initializes the Model class
     */
    private Model() {
    }

    /**
     * Getter for the bounds
     * @return bound
     */
    public TagBound getBound(){return bound;}

    /**
     * Getter for the trie
     * @return trie
     */
    public Trie getTrie(){return trie;}

    /**
     * Getter for the nodes
     * @return nodes
     */
    public TLongObjectHashMap<TagNode> getNodes(){return nodes;}

    /**
     * initializes the static Tree
     * Adds the ways, relations and addresses to the tree clearing them afterwards
     */
    private void initializeTrees(){

        Tree.initializeTree();

        Thread addWays = new Thread(() ->{
            System.out.println("Loading ways");
            long start = System.currentTimeMillis();
            for(TagWay way : XMLReader.getWays().valueCollection()){
                Tree.insertTagWayInTree(way);
            }
            long end = System.currentTimeMillis();
            System.out.println("Time to add ways - total: " + (end - start) + "ms");
            XMLReader.clearWays();
            System.out.println("Cleared ways");
            System.gc();
        });

        Thread addRelations = new Thread(() ->{
            System.out.println("Loading relations");
            long start = System.currentTimeMillis();

            for(TagRelation relation : XMLReader.getRelations().valueCollection()){
                Tree.insertTagRelationInTree(relation);
            }

            long end = System.currentTimeMillis();
            System.out.println("Time to add relations - total: " + (end - start) + "ms");
            XMLReader.clearRelations();
            System.out.println("Cleared relations");
            System.gc();
        });

        Thread addAddresses = new Thread(() ->{
            System.out.println("Loading addresses");
            long start = System.currentTimeMillis();
            for(Tag tag : addresses.valueCollection()){
                Tree.insertTagAdressInTree(tag);
            }

            long end = System.currentTimeMillis();
            System.out.println("Time to add Addresses - total: " + (end - start) + "ms");
            XMLReader.clearAddresses();
            System.out.println("Cleared addresses");
            System.gc();
        });

        addWays.start();
        addRelations.start();
        addAddresses.start();

        try{
            addWays.join();
            addRelations.join();
            addAddresses.join();
        } catch (Exception e) {
            System.out.println(e.getStackTrace()+ " Failed at threadjoining " + e.getMessage());
        }

        Tree.isNowLoaded();
    }
}
