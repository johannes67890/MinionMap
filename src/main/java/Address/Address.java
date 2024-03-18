package Address;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Object class representing addresses
 * 
 * <ul>
 * <li> Is built from userInput </li>
 * <li> Gets modified in order to suit af general format</li>
 * <li>
 * </ul>
 */
public class Address {
    public String street, house, floor, side, postcode, city;

    private Address(
            String _street, String _house, String _floor, String _side,
            String _postcode, String _city) {
        street = _street;
        house = _house;
        floor = _floor;
        side = _side;
        postcode = _postcode;
        city = _city;
    }

    public String toString() {
        return street + " " + house + ", " + floor + " " + side + "\n"
                + postcode + " " + city;
    }

    private final static String REGEX = "(?<street>[A-Za-zØÆÅåæø ]+)? ?(?<house>[0-9]{1,3}[A-Za-z]{0,2})? ?(?<floor> st| [0-9]{1,3})? ?(?<side>tv|th|mf)?\\.? ?(?<postcode>[0-9]{4})? ?(?<city>[A-Za-ØÆÅåøæ ]+)?$"
            ;
    private final static Pattern PATTERN = Pattern.compile(REGEX);


    // "(?<street>[A-Za-zØÆÅåæø ]+)? ?(?<house>[0-9]{1,3}[A-Za-z]{0,30})? ?(?<floor> st| [0-9]{1,3})? ?(?<side>tv|th|mf)?\\p? ?(?<postcode>[0-9]{4})? ?(?<city>[A-Za-ØÆÅåøæ ]+)?"

    // ^(?<street>[A-Za-zØÆÅåæø ]+)? ?(?<house>[0-9]{1,3}[A-Za-z]{0,30})? ?(?<floor> st| [0-9]{1,3})? ?(?<side>tv|th|mf)?\\.? ?(?<postcode>[0-9]{4})? ?(?<city>[A-Za-ØÆÅåøæ ]+)?$

    // (?=(?<street>[.A-Za-zØÆÅåæø ]+)) ?(?=(?<house>[0-9]{1,3}[A-Za-z]{0,2}))? ?(?=(?<floor>st|[0-9]{1,3}[.]{1}))? ?(?=(?<side>tv|th|mf))?\.? ?(?=(?<postcode>[0-9]{4}))? ?(?=(?<city>[A-Za-ØÆÅåøæ ]+))?


    /**
     * 
     * Translates String input into an address.
     * 
     * <ul>
     * <li> Does not require info for all fields (floor, street, etc.)</li>
     * <li> Modifies input for a more fitting format</li>
     * <li> Indirectly instantiates a new Address via the {@link Builder builder's } {@link Builder#build() build method}
     * </ul>
     * @param input any text from user input
     */

    public static Address parse(String input) {
        Builder builder = new Builder();
        Matcher matcher = PATTERN.matcher(input);

        if(matcher.matches()){
            builder.house = matcher.group("house");
            builder.floor = matcher.group("floor");
            builder.side = matcher.group("side");
            builder.postcode = matcher.group("postcode");

            if(matcher.group("floor") != null && !matcher.group("floor").contains(".")){
                builder.floor += ".";
            } else{builder.floor = "";}

            if(matcher.group("side") != null && !matcher.group("side").contains(".")){
                builder.side += ".";
            } else{builder.side = ""; }

            if(matcher.group("street") != null){
                String[] streetSplit = matcher.group("street").split(" ");
                builder.street = "";
                for(String splitString : streetSplit){
                    splitString = splitString.substring(0,1).toUpperCase() +
                    splitString.substring(1).toLowerCase();
                    builder.street += splitString + " ";
                }
                builder.street = builder.street.substring(0, builder.street.length() - 1);
                
            } else{
                builder.street = "";
            }

            if(matcher.group("city") != null){
                String[] citySplit = matcher.group("city").split(" ");
                builder.city = "";
                for(String splitString : citySplit){
                    splitString = splitString.substring(0,1).toUpperCase() +
                    splitString.substring(1).toLowerCase();
                    builder.city += splitString + " ";
                }
                builder.city = builder.city.substring(0, builder.city.length() - 1);
                
            } else{
                builder.city = "";
            }

        } else{
            System.out.println("NO MATCH");
        }
        return builder.build();
    }

    public static class Builder {
        private String street, house, floor, side, postcode, city;

        public Builder street(String _street) {
            street = _street;
            return this;
        }

        public Builder house(String _house) {
            house = _house;
            return this;
        }

        public Builder floor(String _floor) {
            floor = _floor;
            return this;
        }

        public Builder side(String _side) {
            side = _side;
            return this;
        }

        public Builder postcode(String _postcode) {
            postcode = _postcode;
            return this;
        }

        public Builder city(String _city) {
            city = _city;
            return this;
        }


        /**
         * 
         * @return an instance of an address
         */

        public Address build() {
            return new Address(street, house, floor, side, postcode, city);
        }
    }

    // LevenshteinDistance Algorithm: https://commons.apache.org/proper/commons-lang/javadocs/api-2.5/src-html/org/apache/commons/lang/StringUtils.html#line.6162
    // possible method for autocorrect: https://zatackcoder.com/java-program-to-check-two-strings-similarity/



}