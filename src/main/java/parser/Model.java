package parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import gnu.trove.map.hash.TLongObjectHashMap;
import gui.Search;
import structures.Trie;
import structures.KDTree.Tree;

public class Model implements Serializable{
    private static final long serialVersionUID = 1L;

    private static TagBound bound;
    private static TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<TagNode>();
    private static TLongObjectHashMap<TagNode> places = new TLongObjectHashMap<TagNode>();
    private static TLongObjectHashMap<TagRelation> relations = new TLongObjectHashMap<TagRelation>();
    private static TLongObjectHashMap<TagWay> ways = new TLongObjectHashMap<TagWay>();
    private static Trie trie = new Trie();
    private static ArrayList<Tag> tags = new ArrayList<>();


    public Model(XMLReader reader) {
        bound = XMLReader.getBound();
        trie = XMLReader.getTrie();
        nodes = XMLReader.getNodes();
        places = XMLReader.getPlaces();
        relations = XMLReader.getRelations();
        ways = XMLReader.getWays();
        tags.addAll(List.copyOf(ways.valueCollection()));
        tags.addAll(List.copyOf(relations.valueCollection()));
        tags.addAll(List.copyOf(places.valueCollection()));
        tags.addAll(List.copyOf(XMLReader.getAddresses().valueCollection()));
        Tree.initialize(tags);
        clearReadertags();
    }

    public static TagBound getBound(){return bound;}

    public static Trie getTrie(){return trie;}

    public static TLongObjectHashMap<TagNode> getPlaces(){return places;}

    public static TLongObjectHashMap<TagNode> getNodes(){return nodes;}

    public static TLongObjectHashMap<TagWay> getWays(){ return ways; }

    public static TLongObjectHashMap<TagRelation> getRelations(){return relations;}

    private static void clearReadertags(){
        XMLReader.clearTags();
    }
}
