package parser;

import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;

import edu.princeton.cs.algs4.In;
import gnu.trove.map.hash.TLongObjectHashMap;
import structures.Trie;
import structures.KDTree.Tree;


public class Model implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private XMLReader reader;
    private TagBound bound;
    private TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<TagNode>();

    private TLongObjectHashMap<TagAddress> addresses = new TLongObjectHashMap<TagAddress>();
    private Trie trie = new Trie();

    private static Model instanceModel;



    public static Model getInstanceModel(){
        if(instanceModel == null) {
            instanceModel = new Model();
        }

        return instanceModel;
    }

    public static Model updateModelValues(InputStream is){
        return getInstanceModel().setModelValues(is);
    }

    private Model setModelValues(InputStream is){
        this.reader = new XMLReader(is);
        bound = reader.getBound();
        trie = reader.getTrie();
        nodes = reader.getNodes();
        addresses = reader.getAddresses();
        reader.clearTags();
        System.gc();

        System.out.println("Initializing tree: ");
        long startTree = System.currentTimeMillis();
        initializeTrees();
        long stopTree = System.currentTimeMillis();
        System.out.println("Time to tree - total: " + (stopTree - startTree) + "ms");

        return getInstanceModel();
    }

    private Model() {
    }

    public TagBound getBound(){return this.bound;}

    public Trie getTrie(){return trie;}

    public TLongObjectHashMap<TagNode> getNodes(){return nodes;}

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
