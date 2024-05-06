package parser;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamReader;

import gnu.trove.list.linked.TLinkedList;
import util.StringUtility;
import util.Type;

enum Relation {
    ID, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, TYPEVALUE, NAME, RELATIONTYPE
}

public class TagRelation extends Tag implements Serializable {
    private ArrayList<TagNode> nodes = new ArrayList<>();
    private ArrayList<TagRelation> relations = new ArrayList<>();
    private ArrayList<TagWay> ways = new ArrayList<>();
    private ArrayList<TagWay> inner = new ArrayList<>();
    private ArrayList<TagWay> outer = new ArrayList<>();
    private ArrayList<TagWay> actualOuter;
    private ArrayList<TagWay> actualInner;

    private ArrayList<TagWay> handledOuter = new ArrayList<>();
    
    long id;
    String name;
    Type type, relationType;

    public TagRelation(){}

    public TagRelation(XMLBuilder builder){
        this.id = builder.getId();
        this.type = builder.getType();
        this.relationType = builder.getRelationBuilder().getRelationType();
        this.name = builder.getName();
        this.actualOuter = builder.getRelationBuilder().getOuter();
        this.actualInner = builder.getRelationBuilder().getInner();
        this.relations = builder.getRelationBuilder().getRelations();
        this.nodes = builder.getRelationBuilder().getNodes();

        constructOuterWays();
    }

    @Override
    public long getId(){
        return id;
    }

    @Override
    public float getLat() {
        throw new UnsupportedOperationException("TagRelation does not have a latitude value.");
    }
    @Override
    public float getLon() {
        throw new UnsupportedOperationException("TagRelation does not have a longitude value.");
    }


    public void addNode(TagNode node){ nodes.add(node); };
    public void addRelation(TagRelation relation){ relations.add(relation); };
    public void addWay(TagWay way){ ways.add(way); };
    public void addInner(TagWay way){ inner.add(way); };
    public void addOuter(TagWay way){ outer.add(way); /*System.out.println("ADDING OUTER, NEW SIZE: " + outer.size());*/ };
    public void setTypeValue(Type type){ this.type = type; };
    

    public ArrayList<TagNode> getNodes(){ return nodes; };
    public ArrayList<TagRelation> getRelations(){ return relations; };
    public ArrayList<TagWay> getWays(){ return ways; };
    public ArrayList<TagWay> getInner(){ return inner; };
    public ArrayList<TagWay> getOuter(){ return outer; };
    public ArrayList<TagWay> getActualInner(){ return actualInner ; };
    public ArrayList<TagWay> getActualOuter(){ return actualOuter ; };
    public ArrayList<TagWay> getHandledOuter(){ return handledOuter; };

    /**
     * Constructs the outer ways as multiple connected polygons or lines,
     * by assuming that ways with identical start- or endnodes should be merged into one way.
     * 
     */
    public void constructOuterWays(){
        TLinkedList<TagNode> tempNodes = new TLinkedList<>();

        TagNode beginFirstTagNode = null;
        TagNode currentFirstTagNode = null;
        TagNode prevLastTagNode = null;


        for (int j = 0; j < getActualOuter().size() ; j++){
            TagWay outer = getActualOuter().get(j);

            if(outer.loops()){
                TagWay newTagWay = new TagWay(this, outer.getId(), outer.getRefNodes(), outer.getSpeedLimit());
                handledOuter.add(newTagWay);
            } else{
                currentFirstTagNode = outer.getRefNodes().getFirst();
                if (beginFirstTagNode == null){

                    if (j < getActualOuter().size() - 1){

                        TagWay other = getActualOuter().get(j + 1);

                        if ((other.getRefNodes().getFirst().equals(outer.getRefNodes().getLast())) || (other.getRefNodes().getLast().equals(outer.getRefNodes().getLast()))){
                            beginFirstTagNode = outer.getRefNodes().getFirst();
                            currentFirstTagNode = beginFirstTagNode;
                            prevLastTagNode = outer.getRefNodes().getFirst();
                        } 
                        // Starts from the opposite direction
                        else{
                            beginFirstTagNode = outer.getRefNodes().getLast();
                            currentFirstTagNode = beginFirstTagNode;
                            prevLastTagNode = outer.getRefNodes().getFirst();
                        }
                    }
                    else{
                        break;
                    }
                    
                }
                //Checks whether way should be read in reverse
                if ((prevLastTagNode != null) && prevLastTagNode.equals(currentFirstTagNode)){
                    for (TagNode node : outer.getRefNodes()) {
                        TagNode newNode = new TagNode(node);
                        newNode.clearLinks();
                        tempNodes.add(newNode);
                    }
                } else{
                    TLinkedList<TagNode> tempToTempNodes = new TLinkedList<>();
                    for (TagNode node : outer.getRefNodes()) {
                        TagNode newNode = new TagNode(node);
                        newNode.clearLinks();
                        tempToTempNodes.add(newNode);
                    }


                    for (int i = tempToTempNodes.size() - 1; i >= 0; i-- ){
                        TagNode node = new TagNode(tempToTempNodes.get(i)) ;
                        node.clearLinks();
                        tempNodes.add(node);    
                    }
                }

                prevLastTagNode = tempNodes.get(tempNodes.size() - 1);
                if (tempNodes.get(tempNodes.size() - 1).equals(beginFirstTagNode)){
                    TagWay newTagWay = new TagWay(this, id, tempNodes, outer.getSpeedLimit());
                    handledOuter.add(newTagWay);
                    tempNodes = new TLinkedList<>();
                    beginFirstTagNode = null;
                }
            }
        }
    }

    public Type getType() {
        return type;
    }

    public Type getRelationType(){
        return relationType;
    }

    public String getName(){
        return name;
    }
    
    @Override
    public String toString(){
        if(name == null){
            return "Unknown relation " + StringUtility.formatString(this.getType().name()) + " ";
        }
        return name;
    }

    public static class RelationBuilder implements Serializable {
        public TagRelation relation;

        private boolean isEmpty = true;
        private ArrayList<TagNode> nodes = new ArrayList<>();
        private ArrayList<TagRelation> relations = new ArrayList<>();
        private ArrayList<TagWay> ways = new ArrayList<>();
        private ArrayList<TagWay> inner = new ArrayList<>();
        private ArrayList<TagWay> outer = new ArrayList<>();
        private Type RelationType;
        private String TypeValue;

        public void addNode(TagNode node){ nodes.add(node); };
        public void addRelation(TagRelation relation){ relations.add(relation); };
        public void addWay(TagWay way){ ways.add(way); };
        public void addInner(TagWay way){ inner.add(way); };
        public void addOuter(TagWay way){ outer.add(way); };
        public void setRelationType(Type type){ RelationType = type; };
        public void setTypeValue(String value){ TypeValue = value; };


        public ArrayList<TagNode> getNodes(){ return nodes; };
        public ArrayList<TagRelation> getRelations(){ return relations; };
        public ArrayList<TagWay> getWays(){ return ways; };
        public ArrayList<TagWay> getInner(){ return inner; };
        public ArrayList<TagWay> getOuter(){ return outer; };
        public Type getRelationType(){ return RelationType; };
        public String getTypeValue(){ return TypeValue; }

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

        public RelationBuilder parseMember(XMLStreamReader reader) {
            switch (reader.getAttributeValue(null, "type")) {
                case "node":
                    TagNode node = XMLReader.getNodeById(XMLBuilder.getAttributeByLong(reader, "ref"));
                    if(node != null){
                        this.addNode(node);
                        isEmpty = false;
                    }
                    break;
                case "way":
                    long ref = XMLBuilder.getAttributeByLong(reader, "ref");
                    if(XMLReader.getWayById(ref) != null){
                        switch (reader.getAttributeValue(null, "role")) {
                            case "outer":
                                this.addOuter(XMLReader.getWayById(ref));
                                isEmpty = false;
                                break;
                            case "inner":
                                this.addInner(XMLReader.getWayById(ref));
                                isEmpty = false;
                                break;
                            default:
                                this.addWay(XMLReader.getWayById(ref));
                                isEmpty = false;
                                break;
                        }
                    }
                    break;
                case "relation":
                    this.addRelation(XMLReader.getRelationById(XMLBuilder.getAttributeByLong(reader, "ref")));
                    break;
                default:
                    break;
            }
            return this;
        }
        
    }

}
