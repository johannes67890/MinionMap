package structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gnu.trove.map.hash.TCharObjectHashMap;
import parser.TagAddress;

/**
 * Constructor for TrieNode
 * 
 * @param isEnd                marks whether this TrieNode is at the end of a
 *                             branch (ie. a leaf)
 * @param branches             is a HashMap mapping chars to TrieNodes and makes
 *                             up the structure of the Trie
 * @param houseNumberToAddress is a HashMap mapping String keys to Address
 *                             objects
 */
public class TrieNode implements Serializable {
    private boolean isEnd;
    private TCharObjectHashMap<TrieNode> branches;
    private HashMap<String, TagAddress> houseNumberToAddress = new HashMap<>();

    public TrieNode() {
        branches = new TCharObjectHashMap<>();
    }

    public void setIsEnd(boolean bool) {
        isEnd = bool;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public void setNode(char character, TrieNode node) {
        branches.put(character, node);
    }

    public TrieNode getNode(char character) {
        return branches.get(character);
    }

    public void addHouseNumber(String houseNumber, TagAddress address) {
        houseNumberToAddress.put(houseNumber, address);
    }

    public ArrayList<TagAddress> getTagAddresses(){
        return new ArrayList<>(houseNumberToAddress.values());
    }

    public TagAddress getAddressObject(String house) {
        return houseNumberToAddress.get(house);
    }

    public TCharObjectHashMap<TrieNode> getBranches() {
        return branches;
    }

    public boolean containsKey(char character) {
        if (branches.containsKey(character)) {
            return true;
        } else {
            return false;
        }
    }
}
