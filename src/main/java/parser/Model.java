package parser;

import java.io.Serializable;

import gnu.trove.map.hash.TLongObjectHashMap;
import structures.Trie;
import structures.KDTree.Tree;


public class Model implements Serializable{
    private static final long serialVersionUID = 1L;

    private static TagBound bound;
    private static TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<TagNode>();

    private static TLongObjectHashMap<TagAddress> addresses = new TLongObjectHashMap<TagAddress>();
    private static Trie trie = new Trie();



    public Model(XMLReader reader) {
        bound = XMLReader.getBound();
        trie = XMLReader.getTrie();
        nodes = XMLReader.getNodes();
        addresses = XMLReader.getAddresses();

        clearReadertags();
        initializeTrees();
    }



    public static TagBound getBound(){return bound;}

    public static Trie getTrie(){return trie;}

    public static TLongObjectHashMap<TagNode> getNodes(){return nodes;}

    private static void clearReadertags(){
        XMLReader.clearTags();
    }

    private void initializeTrees(){
        Tree.initialize();

        Thread addWays = new Thread(() ->{
            long start = System.currentTimeMillis();
            for(TagWay way : XMLReader.getWays().valueCollection()){
                Tree.insertTagWayInTree(way);
            }
            long end = System.currentTimeMillis();
            System.out.println("Time to add ways - total: " + (end - start) + "ms");
        });

        Thread clearWays = new Thread(() ->{
            XMLReader.clearWays();
            System.out.println("Cleared ways");
            System.gc();
        });

        Thread addRelations = new Thread(() ->{
            long start = System.currentTimeMillis();

            for(TagRelation relation : XMLReader.getRelations().valueCollection()){
                Tree.insertTagRelationInTree(relation);
            }

            long end = System.currentTimeMillis();
            System.out.println("Time to add relations - total: " + (end - start) + "ms");
        });

        Thread clearRelations = new Thread(() ->{
            XMLReader.clearRelations();
            System.out.println("Cleared ways");
            System.gc();
        });

        Thread addAddresses = new Thread(() ->{
            long start = System.currentTimeMillis();
            int t = 0;
            for(Tag tag : addresses.valueCollection()){
                System.out.println(t++);
                Tree.insertTagAdressInTree(tag);
            }

            long end = System.currentTimeMillis();
            System.out.println("Time to add relations - total: " + (end - start) + "ms");
        });

        Thread clearAddresses = new Thread(() ->{
            XMLReader.clearAddresses();
            System.out.println("Cleared ways");
            System.gc();
        });


        addWays.start();
        clearWays.start();
        addRelations.start();
        clearRelations.start();
        addAddresses.start();
        clearAddresses.start();

        try{
            addWays.join();
            clearWays.join();
            addRelations.join();
            clearRelations.join();
            addAddresses.start();
            clearAddresses.start();

        } catch (Exception e) {
            System.out.println(e.getStackTrace()+ " Failed at threadjoining " + e.getMessage());
        }

        Tree.isNowLoaded();
    }
}
