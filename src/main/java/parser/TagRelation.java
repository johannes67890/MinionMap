package parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


enum Relation {
    ID, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, TYPEVALUE, NAME
}

public class TagRelation extends HashMap<Relation, Object>{
    private ArrayList<TagNode> nodes;
    private ArrayList<TagRelation> relations;
    private ArrayList<TagWay> ways;
    private ArrayList<TagWay> inner;
    private ArrayList<TagWay> outer;
    
    public TagRelation(){
        nodes = new ArrayList<>();
        relations = new ArrayList<>();
        ways = new ArrayList<>();
        inner = new ArrayList<>();
        outer = new ArrayList<>();
    }

    public TagRelation(XMLReader.Builder builder){
        super(new HashMap<Relation, Object>(){
            {
                put(Relation.INNER, builder.getRelationBuilder().getRelation().getInner());
                put(Relation.OUTER, builder.getRelationBuilder().getRelation().getOuter());
                put(Relation.WAYS, builder.getRelationBuilder().getRelation().getWays());
                put(Relation.RELATIONS, builder.getRelationBuilder().getRelation().getRelations());
                put(Relation.NODES, builder.getRelationBuilder().getRelation().getNodes());
                put(Relation.TYPEVALUE, builder.getTypeValue());
                put(Relation.TYPE, builder.getType());
                put(Relation.NAME, builder.getName());
            }
        });
    }

    public void addNode(TagNode node){ nodes.add(node); };
    public void addRelation(TagRelation relation){ relations.add(relation); };
    public void addWay(TagWay way){ ways.add(way); };
    public void addInner(TagWay way){ inner.add(way); };
    public void addOuter(TagWay way){ outer.add(way); };
    public void setTypeValue(Type type){ put(Relation.TYPEVALUE, type); };
    
    public ArrayList<TagNode> getNodes(){ return nodes; };
    public ArrayList<TagRelation> getRelations(){ return relations; };
    public ArrayList<TagWay> getWays(){ return ways; };
    public ArrayList<TagWay> getInner(){ return inner; };
    public ArrayList<TagWay> getOuter(){ return outer; };
}
