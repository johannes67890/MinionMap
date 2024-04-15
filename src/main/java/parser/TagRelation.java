package parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamReader;

enum Relation {
    ID, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, TYPEVALUE, NAME, RELATIONTYPE
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
                put(Relation.ID, builder.getId());
                put(Relation.TYPE, builder.getType());
                put(Relation.NAME, builder.getName());
                put(Relation.INNER, builder.getRelationBuilder().getInner());
                put(Relation.OUTER, builder.getRelationBuilder().getOuter());
                put(Relation.WAYS, builder.getRelationBuilder().getWays());
                put(Relation.RELATIONS, builder.getRelationBuilder().getRelations());
                put(Relation.NODES, builder.getRelationBuilder().getNodes());
                put(Relation.RELATIONTYPE, builder.getRelationBuilder().getRelationType());
                put(Relation.TYPEVALUE, builder.getRelationBuilder().getTypeValue());

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
    public void addOuter(TagWay way){ outer.add(way); /*System.out.println("ADDING OUTER, NEW SIZE: " + outer.size());*/ };
    public void setTypeValue(Type type){ put(Relation.TYPEVALUE, type); };
    

    public ArrayList<TagNode> getNodes(){ return nodes; };
    public ArrayList<TagRelation> getRelations(){ return relations; };
    public ArrayList<TagWay> getWays(){ return ways; };
    public ArrayList<TagWay> getInner(){ return inner; };
    public ArrayList<TagWay> getOuter(){ return outer; };
    public ArrayList<TagWay> getActualOuter(){ return (ArrayList<TagWay>) this.get(Relation.OUTER) ; };
    public ArrayList<TagWay> getActualInner(){ return (ArrayList<TagWay>) this.get(Relation.INNER) ; };
    public Type getType() {
        return (Type) this.get(Relation.TYPE);
    }

    public Type getRelationType(){
        return (Type) this.get(Relation.RELATIONTYPE);
    }
    
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
