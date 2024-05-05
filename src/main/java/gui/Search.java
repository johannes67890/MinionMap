package gui;

import java.util.ArrayList;

import parser.TagAddress;
import parser.TagWay;
import parser.Model;
import parser.Tag;
import pathfinding.Dijsktra;
import structures.Trie;
import util.TransportType;
import util.Type;

public class Search {
    private Trie trie;
    private MapView mapView;
    private MainView mainView;
    private Trie.TrieNode pointOfInterestNode = null;

    public Search(MapView mw){
        //mainview to be used with dijsktra
        mapView = mw;
        trie = Model.getTrie();
    }

    /**
     * A method to get the suggestions from the trie
     * @param input - The input string to search for
     * @return an ArrayList of TagAddress objects
     */
    public ArrayList<TagAddress> getSuggestions(String input){
        return trie.getAddressSuggestions(input, 5);
    }

    public void setPointOfInterest(float x, float y){
        TagAddress point = new TagAddress(0, y, x, "PointOfInterest", "1", " ", " ", " ", " ");
        if (pointOfInterestNode == null){
            pointOfInterestNode = trie.insert(point);
        }else{
            pointOfInterestNode.addHouseNumber(" ", point);
        }
    }

    /**
     * A method to get the address object from the trie
     * @param input - The input string to search for
     * @param houseNumber - The house number to search for
     * @return The TagAddress object
     */
    public TagAddress getAddress(String input, String houseNumber){
        return trie.getAddressObject(input, houseNumber);
    }

    /**
     * A method to generate a dijsktra object and pathfind between the two addresses
     * @param start         The start address of pathfinding
     * @param end           The end address of pathfinding
     * @param transportType The type of transportation used to pathfind
     */
    public Dijsktra pathfindBetweenTagAddresses(TagAddress start, TagAddress end, TransportType transportType, boolean shortest){
        Dijsktra dijkstra = new Dijsktra(start, end, transportType, shortest);
        ArrayList<Tag> nodes = new ArrayList<>();
        //nodes.addAll(dijkstra.allVisitedPaths());
        TagWay way = new TagWay((long)0, "Route", dijkstra.shortestPathDetailed(), (short)0, Type.PATHWAY);
        //System.out.println("Total distance: " + dijkstra.getTotalDistance());
        nodes.add(way);
        if (!nodes.isEmpty()){
            mapView.getDrawingMap().setMarkedTag(nodes);
        }
        
        return dijkstra;
    }


}