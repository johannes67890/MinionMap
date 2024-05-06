package parser;

import java.io.Serial;
import java.io.Serializable;

import gnu.trove.map.hash.TLongObjectHashMap;
import structures.Trie;
import structures.KDTree.Tree;


public class Model implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private static TagBound bound;
    private static TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<TagNode>();

    private static TLongObjectHashMap<TagAddress> addresses = new TLongObjectHashMap<TagAddress>();
    private static Trie trie = new Trie();

    private static Model instanceModel;

    private Tree tree;

    static {
        instanceModel = new Model();
    }

    public static Model getInstanceModel(){
        if(instanceModel == null){
            instanceModel = new Model();
        }

        return instanceModel;
    }

    private Model() {
        bound = XMLReader.getBound();
        trie = XMLReader.getTrie();
        nodes = XMLReader.getNodes();
        addresses = XMLReader.getAddresses();

        clearReadertags();
        System.gc();
        System.out.println("Initializing tree: ");
        long startTree = System.currentTimeMillis();
        initializeTrees();
        long stopTree = System.currentTimeMillis();
        System.out.println("Time to tree - total: " + (stopTree - startTree) + "ms");
    }

    public Tree getTree(){
        return this.tree;
    }

    public static TagBound getBound(){return bound;}

    public static Trie getTrie(){return trie;}

    public static TLongObjectHashMap<TagNode> getNodes(){return nodes;}

    private static void clearReadertags(){
        XMLReader.clearTags();
    }

    private void initializeTrees(){

        this.tree = new Tree();

        Thread addWays = new Thread(() ->{
            System.out.println("Loading ways");
            long start = System.currentTimeMillis();
            for(TagWay way : XMLReader.getWays().valueCollection()){
                tree.insertTagWayInTree(way);
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
                tree.insertTagRelationInTree(relation);
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
                tree.insertTagAdressInTree(tag);
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

        tree.isNowLoaded();
    }
}
