package parser;

import java.util.ArrayList;
import java.util.HashMap;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import javax.xml.stream.XMLStreamReader;

enum Relation {
    ID, NAME, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, RELATIONTYPE, RELATIONTYPEVALUE
}

public class TagRelation extends Tag<Relation>{
    TCustomHashMap<Relation, Object> relation = new TCustomHashMap<Relation, Object>();

    public TagRelation(){}
    

    public TagRelation(XMLBuilder builder){
        relation = new TCustomHashMap<Relation, Object>(){
            {
                put(Relation.ID, builder.getId());
                put(Relation.TYPE, Double.parseDouble(builder.getType().toString()));
                put(Relation.NAME, Double.parseDouble(builder.getName()));
                put(Relation.INNER, builder.getRelationBuilder().getInner());
                put(Relation.OUTER, builder.getRelationBuilder().getOuter());
                put(Relation.WAYS, builder.getRelationBuilder().getWays());
                put(Relation.RELATIONS, builder.getRelationBuilder().getRelations());
                put(Relation.NODES, builder.getRelationBuilder().getNodes());
                put(Relation.RELATIONTYPE, builder.getRelationBuilder().getRelationType());
                put(Relation.RELATIONTYPEVALUE, builder.getRelationBuilder().getTypeValue());
            }
        };
    }

    @Override
    public long getId(){
        return Long.parseLong(relation.get(Relation.ID).toString());
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
        return relation.get(Relation.NAME).toString();
    }

    public HashMap<Long, TagWay> getMembers(){
        HashMap<Long, TagWay> members = new HashMap<Long, TagWay>();

        members.putAll(this.getInner().valueCollection().stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll));
        members.putAll(this.getOuter().valueCollection().stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll));
        members.putAll(this.getWays().valueCollection().stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v), HashMap::putAll));

        return members;
        
    }

    public TagWay getMemberById(long id){
        return this.getMembers().get(id);
    }

    public TLongObjectHashMap<TagWay> getInner(){
        return (TLongObjectHashMap<TagWay>) this.get(Relation.INNER);
    }

    public TLongObjectHashMap<TagWay> getOuter(){
        return (TLongObjectHashMap<TagWay>) this.get(Relation.OUTER);
    }

    public TLongObjectHashMap<TagWay> getWays(){
        return (TLongObjectHashMap<TagWay>) this.get(Relation.WAYS);
    }

    public TLongObjectHashMap<TagRelation> getRelations(){
        return (TLongObjectHashMap<TagRelation>) this.get(Relation.RELATIONS);
    }

    public TLongObjectHashMap<TagNode> getNodes(){
        return (TLongObjectHashMap<TagNode>) this.get(Relation.NODES);
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

        public TLongObjectHashMap<TagNode> getNodes(){ return nodes; };
        public TLongObjectHashMap<TagRelation> getRelations(){ return relations; };
        public TLongObjectHashMap<TagWay> getWays(){ return ways; };
        public TLongObjectHashMap<TagWay> getInner(){ return inner; };
        public TLongObjectHashMap<TagWay> getOuter(){ return outer; };
        public Type getRelationType(){ return RelationType; };
        public String getTypeValue(){ return TypeValue; }

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
