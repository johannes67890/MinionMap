package structures;

import java.io.Serializable;
import java.util.ArrayList;

import parser.TagAddress;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import gnu.trove.map.hash.TCharObjectHashMap;
import parser.TagAddress;

public class Trie implements Serializable {
    private TrieNode root;
    private TrieNode currentNode;

    /**
     * Constructor for Trie
     */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts an Address object into the Trie, creating a path by following the
     * branches of the Trie as far as possible,
     * then creating new TrieNodes as necessary. Finally, the TagAddress object, and
     * the String form of the address, is stored at the end of the branch, and the
     * final TrieNode is marked as an end node
     * 
     * @param address the TagAddress object to be inserted
     */
    public TrieNode insert(TagAddress address) {
        // address is made lowercase to make searching easier for the user, ie.
        // writing "Svanevej" is read the same as "svanevej"
        currentNode = root;
        String addressString = address.getStreet() + " " + address.getPostcode() + " " + address.getCity();
        String treatedAddressString = addressString.toLowerCase().replaceAll(" ", "");

        for (int i = 0; i < treatedAddressString.length(); i++) {
            char currentChar = treatedAddressString.charAt(i);
            if (!currentNode.containsKey(currentChar)) {
                currentNode.setNode(currentChar, new TrieNode());
            }
            currentNode = currentNode.getNode(currentChar);
        }
        currentNode.setIsEnd(true);
        currentNode.addHouseNumber(address.getHouseNumber(), address);
        return currentNode;
    }

    /**
     * @param searchInput      the String used to navigate through the Trie
     * @param suggestionAmount the maximum amount of address String suggestions
     *                         needed, ie. the maximum size of the list returned
     * @return a list of possible String addresses for the given input, empty if
     *         there are none
     */
    public ArrayList<TagAddress> getAddressSuggestions(String searchInput, int suggestionAmount) {
        currentNode = root;
        String numbers = searchInput.toLowerCase().replaceAll("[ ,a-zA-Z]", "");
        String searchInputLetters = searchInput.toLowerCase().replaceAll("[ ,]", "");
        ArrayList<TrieNode> suggestionList = new ArrayList<>();
        ArrayList<TagAddress> returnList = new ArrayList<>();

        if (moveThroughTree(searchInputLetters)) {
            
            suggestionFinder(suggestionList, currentNode, suggestionAmount, numbers, 0);

            if (suggestionList.size() == 1){
                returnList.addAll(suggestionList.get(0).getTagAddresses());
            }else{
                for (TrieNode node : suggestionList){
                    TagAddress nodeAddress = node.getTagAddresses().get(0);
                    TagAddress temp = new TagAddress(nodeAddress.getId(), nodeAddress.getLat(), nodeAddress.getLon(), nodeAddress.getStreet(), "", nodeAddress.getPostcode(), nodeAddress.getMunicipality(), nodeAddress.getCity(), nodeAddress.getCountry());
                    returnList.add(temp);
                }
            }
            return returnList;
        }
        return returnList;
    }

    /** 
     * Recursive method for use in getAddressSuggestions()
     * @param suggestionList the list of TrieNodes to be filled with suggestions
     * @param currentNode the current TrieNode being checked
     * @param suggestionAmount the maximum amount of suggestions needed
     * @param iteration the current iteration of the recursive method
     */
    private void suggestionFinder(ArrayList<TrieNode> suggestionList, TrieNode currentNode, int suggestionAmount, String numbers, int iteration) {
        // the additional list size check is necessary for an extreme edge case, but is
        // otherwise not used
        if (currentNode.getIsEnd() && suggestionList.size() < suggestionAmount) {
            suggestionList.add(currentNode);
        }
        if (currentNode.getBranches().isEmpty() || suggestionList.size() >= suggestionAmount) {
            return;
        }
        for (Object obj : currentNode.getBranches().values()) {
            TrieNode branch = (TrieNode) obj;
            suggestionFinder(suggestionList, branch, suggestionAmount, numbers, iteration + 1);
        }
    }

    /**
     * @param searchInput the String address used to navigate through the Trie
     * @param house       the String housenumber for the desired address
     * @return a TagAddress object
     */
    public TagAddress getAddressObject(String searchInput, String house) {

        currentNode = root;
        house = house.replaceAll(" ", "");
        String numbers = searchInput.toLowerCase().replaceAll("[ ,a-zA-Z]", "");
        ArrayList<TrieNode> suggestionList = new ArrayList<>();
        searchInput = searchInput.toLowerCase().replaceAll("[ ,]", "");

        currentNode = root;
        if (moveThroughTree(searchInput)) {

            suggestionFinder(suggestionList, currentNode, 5, numbers, 0);
           
            return suggestionList.get(0).getAddressObject(house);

        } else {
            return null;
        }
    }

    private boolean moveThroughTree(String searchInput) {
        currentNode = root;
        searchInput = searchInput.toLowerCase().replaceAll(" ", "");
        for (int i = 0; i < searchInput.length(); i++) {
            char currentChar = searchInput.charAt(i);
            if (currentNode.containsKey(currentChar)) {
                currentNode = currentNode.getNode(currentChar);
            } else {
                currentNode = root;
                return false;
            }
        }
        return true;
    }

/**
 * Constructor for TrieNode
 * 
 * @param isEnd                marks whether this TrieNode is at the end of a
 *                             branch (ie. a leaf)
 * @param branches             is a HashMap mapping chars to TrieNodes and makes
 *                             up the structure of the Trie
 * @param houseNumberToAddress is a HashMap mapping String keys to Address
 *                             objects
 * @param endAddress           is a String of the full address contained in the
 *                             Trie, not including the housenumber
 */
public class TrieNode implements Serializable {
    private boolean isEnd;
    private TCharObjectHashMap<TrieNode> branches;
    private HashMap<String, TagAddress> houseNumberToAddress = new HashMap<>();
    private String endAddress;

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

    public ArrayList<String> getHouseNumbers() {
        ArrayList<String> houseNumberList = new ArrayList<>(houseNumberToAddress.keySet());
        return houseNumberList;
    }

    public void setEndAddress(String address) {
        this.endAddress = address;
    }

    public ArrayList<TagAddress> getTagAddresses(){
        return new ArrayList<>(houseNumberToAddress.values());
    }

    public String getEndAddress() {
        return endAddress;
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

}
