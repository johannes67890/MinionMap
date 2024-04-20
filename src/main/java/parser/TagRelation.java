package parser;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamReader;

enum Relation {
    ID, INNER, OUTER, WAYS, RELATIONS, NODES, TYPE, TYPEVALUE, NAME, RELATIONTYPE
}

public class TagRelation extends Tag{
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
        //this.ways = builder.getRelationBuilder().getWays();


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
    public ArrayList<TagWay> getActualOuter(){ return actualOuter ; };
    public ArrayList<TagWay> getHandledOuter(){ return handledOuter; };


    /**
     * Constructs the outer ways as multiple connected polygons or lines,
     * by assuming that ways with identical start- or endnodes should be merged into one way.
     * 
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

        int wayCount = 0;


        for (int j = 0; j < getActualOuter().size() ; j++){

            TagWay outer = getActualOuter().get(j);

            speedLimit = outer.getSpeedLimit();
            success = false;
            id = outer.getId();

            if(outer.loops()){
                success = true;
                handledOuter.add(outer);
            } else{

                currentFirstTagNode = outer.firsTagNode();


                if (beginFirstTagNode == null){

                    if (j < getActualOuter().size() - 1){

                        TagWay other = getActualOuter().get(j + 1);

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
                    else{
                        break;
                    }
                    
                }
                //Checks whether way should be read in reverse

                if ((prevLastTagNode != null) && prevLastTagNode.equals(currentFirstTagNode)){

                    for (TagNode node : outer.getNodes()){
                        tempNodes.add(node);    
                    }
                } else{
                    for (int i = outer.getNodes().length - 1; i >= 0; i-- ){

                        TagNode node = outer.getNodes()[i];

                        tempNodes.add(node);    
                    }
                }

                wayCount++;

                prevLastTagNode = tempNodes.get(tempNodes.size() - 1);


                if (tempNodes.get(tempNodes.size() - 1).equals(beginFirstTagNode)){

                    //System.out.println(beginLastTagNode.getId() + " " + outer.getId());


                    TagNode[] nodes = tempNodes.toArray(new TagNode[tempNodes.size()]);

                    TagWay newTagWay = new TagWay(this, id, nodes, speedLimit);
                    handledOuter.add(newTagWay);
                    tempNodes.clear();
                    tempNodes = new ArrayList<>();
                    beginFirstTagNode = null;
                    beginLastTagNode = null;
                    success = true;


                    if (name != null && name.equals("Bornholm")){
                        System.out.println("Fyn");
                        System.out.println(type.getKey() + " " + type.getValue() + " " + wayCount);


                    }

                    wayCount = 0;


                }
            }
        }
        /*if (!success){
            TagWay newTagWay = new TagWay(this, id, tempNodes, speedLimit);
            handledOuter.add(newTagWay);
            tempNodes = new ArrayList<>();
        }*/
    }



    
    public ArrayList<TagWay> getActualInner(){ return actualInner ; };

    public Type getType() {
        return type;
    }

    public Type getRelationType(){
        return relationType;
    }

    public String getName(){
        return name;
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
