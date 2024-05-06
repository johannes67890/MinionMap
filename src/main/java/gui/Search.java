package gui;

import java.util.ArrayList;

import parser.Model;
import parser.TagAddress;
import pathfinding.Dijkstra;
import structures.Trie;
import util.TransportType;

 

public class Search {
    private Trie trie;

    public Search(MapView mw){
        trie = Model.getInstanceModel().getTrie();
    }

    public Search(Trie trie){
        this.trie = trie;
    }

    /**
     * A method to get the suggestions from the trie
     * @param input - The input string to search for
     * @return an ArrayList of TagAddress objects
     */
    public ArrayList<TagAddress> getSuggestions(String input){
        return trie.getAddressSuggestions(input, 5);
    }

    /**
     * A method to get the address object from the trie
     * @param input - The input string to search for
     * @param houseNumber - The house number to search for
     * @return The TagAddress object
     */
    public TagAddress getAddress(String input, String houseNumber){
        System.out.println(input);
        return trie.getAddressObject(input, houseNumber);
    }

    /**
     * A method to generate a dijsktra object and pathfind between the two addresses
     * @param start         The start address of pathfinding
     * @param end           The end address of pathfinding
     * @param transportType The type of transportation used to pathfind
     * @param shortest      A boolean to determine if the shortest or fastest path should be found
     */
    public Dijkstra pathfindBetweenTagAddresses(TagAddress start, TagAddress end, TransportType transportType, boolean shortest){
        Dijkstra dijkstra = new Dijkstra(start, end, transportType, shortest);
        
        return dijkstra;
    }


}