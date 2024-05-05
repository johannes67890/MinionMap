package parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import gnu.trove.map.hash.TLongObjectHashMap;
import structures.Trie;
import structures.KDTree.Tree;

public class Model implements Serializable{

    private TagBound bound;
    private TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<TagNode>();
    private TLongObjectHashMap<TagAddress> addresses = new TLongObjectHashMap<TagAddress>();
    private TLongObjectHashMap<TagRelation> relations = new TLongObjectHashMap<TagRelation>();
    private TLongObjectHashMap<TagWay> ways = new TLongObjectHashMap<TagWay>();
    private Trie trie = new Trie();
    private Tree tree = new Tree();
    private ArrayList<Tag> tags = new ArrayList<>();


    public Model(XMLReader reader) {


        this.bound = XMLReader.getBound();
        this.trie = XMLReader.getTrie();
        this.nodes = XMLReader.getNodes();
        
        this.relations = XMLReader.getRelations();
        
        this.ways = XMLReader.getWays();

        tags.addAll(List.copyOf(ways.valueCollection()));
        tags.addAll(List.copyOf(relations.valueCollection()));
        System.out.println(tags.size());
        Tree.initialize(tags);

        clearReadertags();

    }

    public TagBound getBound(){return bound;}

    public TLongObjectHashMap<TagNode> getNodes(){return nodes;}

    public TLongObjectHashMap<TagAddress> getAddresses(){return  addresses;}

    public TLongObjectHashMap<TagWay> getWays(){
        return ways;
    }

    public TLongObjectHashMap<TagRelation> getRelations(){return relations;}

    private void clearReadertags(){

        XMLReader.clearTags();

    }
}
