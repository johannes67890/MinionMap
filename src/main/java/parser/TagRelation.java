package parser;

import java.util.ArrayList;
import java.util.HashMap;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import javax.xml.stream.XMLStreamReader;

enum Relation {
    ID, NAME, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, RELATIONTYPE, RELATIONTYPEVALUE
}

public class TagRelation extends Tag<Relation, THashMap<Relation, Object>>{

    private THashMap<Relation, Object> relation = new THashMap<Relation, Object>();

    public TagRelation(XMLBuilder builder){
        relation = new THashMap<Relation, Object>(){
            {
                put(Relation.ID, builder.getId());
                put(Relation.TYPE, builder.getType());
                put(Relation.NAME, builder.getName());
                put(Relation.INNER, builder.getRelationBuilder().getInner());
                put(Relation.OUTER, builder.getRelationBuilder().getOuter());
                put(Relation.WAYS, builder.getRelationBuilder().getWays());
                put(Relation.RELATIONS, builder.getRelationBuilder().getRelations());
                put(Relation.NODES, builder.getRelationBuilder().getNodes());
                put(Relation.RELATIONTYPE, builder.getRelationBuilder().getRelationType());
                put(Relation.RELATIONTYPEVALUE, builder.getRelationBuilder().getTypeValue());
            }
        };
        constructOuterWays();

    }

    @Override
    public long getId() {return (long) this.relation.get(Relation.ID); }

    @Override
    public double getLat() {
        throw new UnsupportedOperationException("TagRelation does not have a latitude value.");
    }

    @Override
    public double getLon() {
        throw new UnsupportedOperationException("TagRelation does not have a longitude value.");
    }
    
    @Override
    public boolean isEmpty(){ return this.relation.isEmpty(); }

    @Override
    public THashMap<Relation, Object> getMap() { return this.relation; }
    
    public String getName(){ return relation.get(Relation.NAME).toString(); }
    public TagWay getMemberById(long id){ return this.getMembers().get(id); }
    public Type getType(){ return  (Type) this.relation.get(Relation.TYPE); }
    public Type getRelationType(){ return (Type) this.relation.get(Relation.RELATIONTYPE); }
    public String getTypeValue(){ return this.relation.get(Relation.RELATIONTYPEVALUE).toString(); }

    public TLongObjectHashMap<TagWay> getInner(){ return (TLongObjectHashMap<TagWay>) this.relation.get(Relation.INNER); }
    public TLongObjectHashMap<TagWay> getOuter(){ return (TLongObjectHashMap<TagWay>) this.relation.get(Relation.OUTER); }
    public TLongObjectHashMap<TagWay> getWays(){ return (TLongObjectHashMap<TagWay>) this.relation.get(Relation.WAYS); }
    public TLongObjectHashMap<TagRelation> getRelations(){ return (TLongObjectHashMap<TagRelation>) this.relation.get(Relation.RELATIONS); }
    public TLongObjectHashMap<TagNode> getNodes(){ return (TLongObjectHashMap<TagNode>) this.relation.get(Relation.NODES); }

    public TLongObjectHashMap<TagWay> getMembers(){
        TLongObjectHashMap<TagWay> members = new TLongObjectHashMap<TagWay>();
        members.putAll(getInner());
        members.putAll(getOuter());
        members.putAll(getWays());
        return members;
    }

    private ArrayList<TagWay> handledOuter = new ArrayList<>();
    public ArrayList<TagWay> getHandledOuter(){ return handledOuter; };

    /**
     * Constructs the outer ways as multiple connected polygons or lines,
     * by assuming that ways with identical start- or endnodes should be merged into one way.
     */
    public void constructOuterWays(){

        ArrayList<TagNode> tempNodes = new ArrayList<>();
        TagNode beginLastTagNode = null;
        TagNode beginFirstTagNode = null;

        TagNode currentLastTagNode = null;
        TagNode currentFirstTagNode = null;

        TagNode prevLastTagNode = null;

        boolean success = false;
        int speedLimit = 0;
        long id = 0;

        for (int j = 0; j < getOuter().size() ; j++){
            TagWay outer = getOuter().get(j);

            if(outer.Looped()){
                handledOuter.add(outer);
                continue;
            } 

            currentFirstTagNode = outer.firsTagNode();


            if (beginFirstTagNode == null){
                    TagWay other = getOuter().get(j + 1);

                    if ((other.firsTagNode().equals(outer.lastTagNode())) || (other.lastTagNode().equals(outer.lastTagNode()))){

                        beginFirstTagNode = outer.firsTagNode();
                        currentFirstTagNode = beginFirstTagNode;
                        beginLastTagNode = outer.lastTagNode();
                        prevLastTagNode = outer.firsTagNode();
                    } 
                    // Starts from the opposite direction
                    else{

                        beginFirstTagNode = outer.lastTagNode();
                        currentFirstTagNode = beginFirstTagNode;
                        beginLastTagNode = outer.firsTagNode();
                        prevLastTagNode = outer.firsTagNode();
                    }
            }


            //Checks whether way should be read in reverse
            if (prevLastTagNode != null && prevLastTagNode.equals(currentFirstTagNode)){

                for (TagNode node : outer.getRefs()){


                    tempNodes.add(node);    
                }
            } else {
                for (int i = outer.getRefs().size() - 1; i >= 0; i-- ){

                    TagNode node = outer.getRefs().get(i);

                    tempNodes.add(node);    
                }
            }
            prevLastTagNode = tempNodes.get(tempNodes.size() - 1);


            if (tempNodes.get(tempNodes.size() - 1).equals(beginFirstTagNode)){

                TagNode[] nodes = tempNodes.toArray(new TagNode[tempNodes.size()]);

                TagWay newTagWay = new TagWay(this, id, nodes, speedLimit);
                handledOuter.add(newTagWay);
                tempNodes.clear();
                tempNodes = new ArrayList<>();
                beginFirstTagNode = null;
                beginLastTagNode = null;
                success = true;
            }
        }
    }


    public static class RelationBuilder {
        private boolean isEmpty = true;
        private TLongObjectHashMap<TagNode> nodes = new TLongObjectHashMap<>();
        private TLongObjectHashMap<TagRelation> relations = new TLongObjectHashMap<>();
        private TLongObjectHashMap<TagWay> ways = new TLongObjectHashMap<>();
        private TLongObjectHashMap<TagWay> inner = new TLongObjectHashMap<>();
        private TLongObjectHashMap<TagWay> outer = new TLongObjectHashMap<>();
        private Type RelationType;
        private String TypeValue;

        public void setRelationType(Type currType) {
            RelationType = currType;
        }
        public void setTypeValue(String v) {
            TypeValue = v;
        }

        public Type getRelationType(){ return RelationType; };
        public String getTypeValue(){ return TypeValue; };
        public TLongObjectHashMap<TagNode> getNodes(){ return nodes; };
        public TLongObjectHashMap<TagRelation> getRelations(){ return relations; };
        public TLongObjectHashMap<TagWay> getWays(){ return ways; };
        public TLongObjectHashMap<TagWay> getInner(){ return inner; };
        public TLongObjectHashMap<TagWay> getOuter(){ return outer; };

        public boolean isEmpty() {
            return isEmpty;
        }

        public RelationBuilder parseMember(XMLStreamReader reader) {
            long ref = XMLBuilder.getAttributeByLong(reader, "ref");

            switch (reader.getAttributeValue(null, "type")) {
                case "node":
                    nodes.put(ref, XMLReader.getNodeById(ref));
                    isEmpty = false;
                    break;
                case "way":
                    if(XMLReader.getWayById(ref) != null){
                        switch (reader.getAttributeValue(null, "role")) {
                            case "outer":
                                outer.put(ref, XMLReader.getWayById(ref));
                                isEmpty = false;
                                break;
                            case "inner":
                                inner.put(ref, XMLReader.getWayById(ref));
                                isEmpty = false;
                                break;
                            default:
                                ways.put(ref, XMLReader.getWayById(ref));
                                isEmpty = false;
                                break;
                        }
                    }
                    break;
                case "relation":
                    relations.put(ref, XMLReader.getRelationById(ref));
                    break;
                default:
                    break;
            }
            return this;
        }
    }

}
