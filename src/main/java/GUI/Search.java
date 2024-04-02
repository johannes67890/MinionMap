package gui;

import java.util.ArrayList;

import org.parser.TagAddress.SearchAddress;
import org.parser.TagAddress;
import org.parser.XMLReader;

import javafx.scene.control.TextArea;

public class Search {

    ArrayList<String> cityNames, streetNames, postCodes;
    ArrayList<TagAddress> addresses;
    XMLReader reader;

    public Search(ArrayList<TagAddress> addresses){
        this.addresses = addresses;
        readFiles();
    }

    /**
     * Reads files used later in {@link Main its class}
     */
    public void readFiles(){


        cityNames = new ArrayList<>();
        streetNames = new ArrayList<>();
        postCodes = new ArrayList<>();
        String current;
        String[] splitCurrent;

        //Adding addresses from XMLReader into the lists
        for (int i = 0; i < addresses.size(); i++){
            cityNames.add(addresses.get(i).getCity());
            streetNames.add(addresses.get(i).getStreet());
            postCodes.add(addresses.get(i).getPostcode());
        }
        
        /*try{

            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/citynames.txt"));
            while (reader.ready()) {
                cityNames.add(reader.readLine());
            }
            reader.close();
            reader = new BufferedReader(new FileReader("src/main/resources/streetnames.txt"));
            while (reader.ready()){
                streetNames.add(reader.readLine());
            }
            reader.close();
            reader = new BufferedReader(new FileReader("src/main/resources/postnumre.txt"));
            while (reader.ready()){
                current = reader.readLine();
                splitCurrent = current.split(" ");

                postCodes.add(splitCurrent[0]);
            }
            reader.close();
        
        } catch(IOException e){
            System.out.println("FILE NOT FOUND");
        }*/
    }


    /**
     * 
     * Looks for single resembling components for the address (streets, cities, postcodes)
     * Also creates a new address based on input
     * 
     * @param input String that will be contructed into an address.
     */
    public void searchForAdress(String input, TextArea output){
        SearchAddress a = new SearchAddress(input);
            output.setText(a.toString());

            long time = System.currentTimeMillis();

            if(cityNames.contains(a.city)){
                System.out.println("CITY FOUND: " + a.city);
            } else{

                String topString = findSimilar(cityNames, a.city);

                if (topString != null){

                    System.out.println("Mente du: " + topString + "?");

                } else{System.out.println("This City: " + a.city + " does not exist");}

            }
            if(streetNames.contains(a.street)){
                System.out.println("STREET FOUND: " + a.street);
            } else{

                String topString = findSimilar(streetNames, a.street);

                if (topString != null){
                    System.out.println("Mente du: " + topString + "?");
                } else{System.out.println("This City: " + a.street + " does not exist");}

            } 
            System.out.println("Time: " + (System.currentTimeMillis() - time));
            System.out.println(a.street);
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
        String topString = null;

        int maxSim = Integer.MAX_VALUE;
        int current;

        for (String cityName : cityNames){
            current = util.StringUtility.getLevenshteinDistance(cityName, s);
            if (current < maxSim){
                maxSim = current;
                topString = cityName;
            }
        }
        return topString;
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



}
