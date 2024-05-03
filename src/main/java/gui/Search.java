package gui;

import java.util.ArrayList;

import parser.TagAddress;
import parser.TransportType;
import parser.XMLReader;
import pathfinding.Dijsktra;
import util.Trie;

public class Search {
    private Trie trie;
    private MainView mainView;

    public Search(MainView mw){
        //mainview to be used with dijsktra
        mainView = mw;
        trie = XMLReader.getTrie();
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
        return trie.getAddressObject(input, houseNumber);
    }

    /**
     * A method to generate a dijsktra object and pathfind between the two addresses
     * @param start         The start address of pathfinding
     * @param end           The end address of pathfinding
     * @param transportType The type of transportation used to pathfind
     */
    public void pathfindBetweenTagAddresses(TagAddress start, TagAddress end, TransportType transportType){
        // Dijsktra djiktra = new Dijsktra(start, end, transportType);
        // TLinkedList<TagNode> nodes = new TLinkedList<>();
        // TagWay way = new TagWay(0, "Route", nodes, (short)0, null);
        // mainView.getDrawingMap().setMarkedTag(way);
    }
}