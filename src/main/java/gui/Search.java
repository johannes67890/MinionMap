package gui;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Stack;
import gnu.trove.list.linked.TLinkedList;
import gnu.trove.map.hash.TLongObjectHashMap;
import parser.TagAddress;
import parser.TransportType;
import parser.TagAddress.SearchAddress;
import parser.TagNode;
import parser.TagWay;
import parser.XMLReader;
import pathfinding.Dijsktra;
import util.Trie;

public class Search {

    private ArrayList<String> cityNames, streetNames, postCodes;
    private TLongObjectHashMap<TagAddress> addresses;
    private Trie trie;
    private MainView mainView;

    public Search(MainView mw){
        mainView = mw;
        this.addresses = XMLReader.getAddresses();
        readFiles();
        trie = XMLReader.getTrie();
    }

    /**
     * Reads files used later in {@link Main its class}
     */
    public void readFiles(){

        //TODO make these into a hashmap?
        cityNames = new ArrayList<>();
        streetNames = new ArrayList<>();
        postCodes = new ArrayList<>();

        //Adding addresses from XMLReader into the lists
        for (TagAddress a : addresses.valueCollection().stream().toList()){
            cityNames.add(a.getCity());
            streetNames.add(a.getStreet());
            postCodes.add(a.getPostcode());
        }
    }


    /**
     * 
     * Looks for single resembling components for the address (streets, cities, postcodes)
     * Also creates a new address based on input
     * 
     * @param input String that will be contructed into an address.
     */
    public SearchAddress searchForAddress(String input){
        SearchAddress a = new SearchAddress(input);
        String street = a.street;
        String house = a.house;
        String floor = a.floor;
        String side = a.side;
        String postcode = a.postcode;
        String city = a.city;
        long time = System.currentTimeMillis();

        if(cityNames.contains(a.city)){
            city = a.city;
            //System.out.println("CITY FOUND: " + a.city);
        } else{

            String topString = findSimilar(cityNames, a.city);

            if (topString != null){

                //System.out.println("Mente du: " + topString + "?");
                a.city = topString;

            } else{
                //System.out.println("This City: " + a.city + " does not exist");
            }

        }
        if(streetNames.contains(a.street)){
            street = a.street;
            
        } else{

            String topString = findSimilar(streetNames, a.street);

            if (topString != null){
                //System.out.println("Mente du: " + topString + "?");
                a.street = topString;
            } else{
                //System.out.println("This City: " + a.street + " does not exist");
            }

        } 
        //System.out.println(a.street);

        a = new SearchAddress(street + " " + house + " " + floor + " " + side + " " + postcode + " " + city);
        return a;
    }

    /**
     * Finds the most similar string in a list with another string
     * 
     * <ul>
     * <li>Note that some strings might be equally similar</li>
     * </ul>
     * @param list the list to compare with the string
     * @param s the string to be compared
     * @return a string in the list that resembles s the most
     */
    public String findSimilar(ArrayList<String> list, String s){
        String nearestWord = null;

        int maxSim = Integer.MAX_VALUE;
        int current;

        for (String comparatorString : list){
            current = util.StringUtility.getLevenshteinDistance(comparatorString, s);
            if (current < maxSim){
                maxSim = current;
                nearestWord = comparatorString;
            }
        }
        return nearestWord;
    }

    public ArrayList<String> getCities(){
        return cityNames;
    }
    public ArrayList<String> getStreets(){
        return streetNames;
    }
    public ArrayList<String> getPostCodes(){
        return postCodes;
    }


    //TODO: Only find the first instance of a street with the name. Turn into average lat and lon?
    public double getLatitudeByStreet(String street){
        for(TagAddress a : addresses.valueCollection().stream().toList()){
            if(a.getStreet().equals(street)){
                return a.getLat();
            }
        }
        return 0;
    }

    public double getLongitudeByStreet(String street){
        for(TagAddress a : addresses.valueCollection().stream().toList()){
            if(a.getStreet().equals(street)){
                return a.getLon();
            }
        }
        return 0;
    }

    public TagAddress getTagAddressByAddress(SearchAddress searchAddress){
        TagAddress best = null;
        int depth = 0;
        for(TagAddress tagAddress : addresses.valueCollection().stream().toList()){
            if((searchAddress.city.isBlank() && searchAddress.postcode.isBlank())||tagAddress.getPostcode().equals(searchAddress.postcode) || tagAddress.getCity().equals(searchAddress.street)){
                if(depth < 1){
                    best = tagAddress;
                    depth = 1;
                }
                if((searchAddress.street.isBlank())||tagAddress.getStreet().equals(searchAddress.street)){
                    if(depth < 2){
                        best = tagAddress;
                        depth = 2;
                    }
                    if(tagAddress.getHouseNumber().equals(searchAddress.house)){
                        System.out.println("THIS IS THE STREET " + tagAddress.getStreet());
                        return tagAddress;
                    }
                }
            }
        }
        return best;
    }

    public ArrayList<TagAddress> getSuggestions(String input){
        return trie.getAddressSuggestions(input, 5);
    }

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
        Dijsktra djiktra = new Dijsktra(start, end, transportType);
        TagNode startNode = djiktra.getNearestRoadPoint(start);
        TagNode endNode = djiktra.getNearestRoadPoint(end);
        TLinkedList<TagNode> nodes = djiktra.pathToTagInNodes(endNode);
        System.out.println(nodes.size());
        TagWay way = new TagWay(0, "Route", nodes, (short)0, null);
        mainView.getDrawingMap().setMarkedTag(way);
    }
}