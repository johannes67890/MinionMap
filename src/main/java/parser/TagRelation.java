package parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamReader;

enum Relation {
    ID, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, TYPEVALUE, NAME
}

public class TagRelation extends Tag<Relation>{
    private ArrayList<TagNode> nodes = new ArrayList<>();
    private ArrayList<TagRelation> relations = new ArrayList<>();
    private ArrayList<TagWay> ways = new ArrayList<>();
    private ArrayList<TagWay> inner = new ArrayList<>();
    private ArrayList<TagWay> outer = new ArrayList<>();

    public TagRelation(){}

    public TagRelation(XMLBuilder builder){
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

    @Override
    public long getId(){
        return Long.parseLong(this.get(Relation.ID).toString());
    }
    @Override
    public double getLat() {
        throw new UnsupportedOperationException("TagRelation does not have a latitude value.");
    }
    @Override
    public double getLon() {
        throw new UnsupportedOperationException("TagRelation does not have a longitude value.");
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
    
    // https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm
    public void ringAssignment(){
        //RA1
        int c = 0;
        HashMap<TagWay, Boolean> relationWays = new HashMap<TagWay, Boolean>();
        // collect all ways that are members of the relation and mark them as not assigned
        relationWays.putAll(ways.stream().collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll));
        relationWays.putAll(inner.stream().collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll));
        relationWays.putAll(outer.stream().collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll));

        relationWays.forEach((way, assigned) -> {
            if(assigned) return;
            else {
                TagWay assignedWay = way;
                assigned = true;
            }
        });
    }

    public static class RelationBuilder {
        private boolean isEmpty;
        public TagRelation relation;

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

        public void parseMember(XMLStreamReader reader) {
            switch (reader.getAttributeValue(null, "type")) {
                case "node":
                    TagNode node = XMLReader.getNodeById(XMLBuilder.getAttributeByLong(reader, "ref"));
                    if(node != null){
                        relation.addNode(node);
                    }
                    break;
                case "way":
                    long ref = XMLBuilder.getAttributeByLong(reader, "ref");
                    if(XMLReader.getWayById(ref) != null){
                        switch (reader.getAttributeValue(null, "role")) {
                            case "outer":
                                relation.addOuter(XMLReader.getWayById(ref));
                                break;
                            case "inner":
                                relation.addInner(XMLReader.getWayById(ref));
                                break;
                            default:
                                relation.addWay(XMLReader.getWayById(ref));
                                break;
                        }
                    }
                    break;
                case "relation":
                    relation.addRelation(XMLReader.getRelationById(XMLBuilder.getAttributeByLong(reader, "ref")));
                    break;
                default:
                    break;
            }
        }
        
    }

}
