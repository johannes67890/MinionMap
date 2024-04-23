package util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import parser.TagAddress;

/**
 * Constructor for TrieNode
 * @param isEnd marks whether this TrieNode is at the end of a branch (ie. a leaf)
 * @param branches is a HashMap mapping chars to TrieNodes and makes up the structure of the Trie
 * @param houseNumberToAddress is a HashMap mapping String keys to Address objects
 * @param endAddress is a String of the full address contained in the Trie, not including the housenumber
 */
public class TrieNode implements Serializable {
    private boolean isEnd;
    private HashMap<Character, TrieNode> branches;
    private HashMap<String, TagAddress> houseNumberToAddress = new HashMap<>();
    private String endAddress;

    public TrieNode(){
        branches = new HashMap<>();
    }

    public void setIsEnd(boolean bool){
        isEnd = bool;
    }

    public boolean getIsEnd(){
        return isEnd;
    }

    public void setNode(char character, TrieNode node){
        branches.put(character, node);
    }

    public TrieNode getNode(char character){
        return branches.get(character);
    }

    public void addHouseNumber(String houseNumber, TagAddress address){
        houseNumberToAddress.put(houseNumber, address);
    }

    public ArrayList<String> getHouseNumbers(){
        ArrayList<String> houseNumberList = new ArrayList<>(houseNumberToAddress.keySet());
        return houseNumberList;
    }

    public void setEndAddress(String address) {
        this.endAddress = address;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public TagAddress getAddressObject(String house){
        return houseNumberToAddress.get(house);
    }

    public HashMap<Character, TrieNode> getBranches(){
        return branches;
    }

    public boolean containsKey(char character){
        if (branches.containsKey(character)){
            return true;
        }
        else {
            return false;
        }
    }
}
