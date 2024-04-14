package parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamReader;

enum Relation {
    ID, NAME, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, RELATIONTYPE, RELATIONTYPEVALUE
}

public class TagRelation extends Tag<Relation>{
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
                put(Relation.RELATIONTYPEVALUE, builder.getRelationBuilder().getTypeValue());
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
    
    public String getName(){
        return this.get(Relation.NAME).toString();
    }

    public HashMap<Long, TagWay> getMembers(){
        HashMap<Long, TagWay> members = new HashMap<Long, TagWay>();

        members.putAll(this.getInner().stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll));
        members.putAll(this.getOuter().stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll));
        members.putAll(this.getWays().stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll));

        return members;
        
    }

    public TagWay getMemberById(long id){
        return this.getMembers().get(id);
    }

    public ArrayList<TagWay> getInner(){
        return (ArrayList<TagWay>) this.get(Relation.INNER);
    }

    public ArrayList<TagWay> getOuter(){
        return (ArrayList<TagWay>) this.get(Relation.OUTER);
    }

    public ArrayList<TagWay> getWays(){
        return (ArrayList<TagWay>) this.get(Relation.WAYS);
    }

    public ArrayList<TagRelation> getRelations(){
        return (ArrayList<TagRelation>) this.get(Relation.RELATIONS);
    }

    public ArrayList<TagNode> getNodes(){
        return (ArrayList<TagNode>) this.get(Relation.NODES);
    }

    public String getType(){
        return this.get(Relation.TYPE).toString();
    }

    public Type getRelationType(){
        return (Type) this.get(Relation.RELATIONTYPE);
    }

    public String getTypeValue(){
        return this.get(Relation.RELATIONTYPEVALUE).toString();
    }



    // https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm
    // public void ringAssignment(){
    //     //RA1
    //     int c = 0;
    //     HashMap<TagWay, Boolean> relationWays = new HashMap<TagWay, Boolean>();
    //     // collect all ways that are members of the relation and mark them as not assigned
    //     relationWays.putAll(ways.stream().collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll));
    //     relationWays.putAll(inner.stream().collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll));
    //     relationWays.putAll(outer.stream().collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll));

    //     relationWays.forEach((way, assigned) -> {
    //         if(assigned) return;
    //         else {
    //             TagWay assignedWay = way;
    //             assigned = true;
    //         }
    //     });
    // }

    public static class RelationBuilder {
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

        public boolean isEmpty() {
            return isEmpty;
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
