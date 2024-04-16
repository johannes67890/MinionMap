package util;

/**
 * 
 * UtilityClass for containing various methods for handling strings.
 * 
 */
public final class StringUtility {


    /**
     * 
     * Calculates how much input to make two string identical, based on Levenshtein's distance calculation.
     * @param s The first string, and initially the smallest
     * @param t The second string, and initially the biggest
     * @return an integer, that counts how much input is needed to make the two strings identical
     */
    public static int getLevenshteinDistance(String s, String t) {

        if (s == null || t == null){
            throw new IllegalArgumentException("Strings must not be null");
        }

        int n = s.length(); // length of first string
        int m = t.length(); // length of second

        /*The distance will be the length of the other string
        as long as one is length 0.
        Since it will take "length" additions on symbols to change one or the other.
        */
        if (n == 0){
            return m;
        } else if (m == 0 ){
            return n;
        }

        if (n > m){
            // swaps strings.
            //System.out.print("SWAPPING STRINGS");
            String tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }

        int p[] = new int[n+1];
        int d[] = new int[n+1];
        int _d[];

        //indexes, one for each string
        int i;
        int j;

        //jth charcter of the second string
        char t_j;

        int cost;

        for (i = 0; i <= n; i++){
            p[i] = i;
        }

        for (j = 1; j <= m; j++){
            t_j = t.charAt(j-1);
            d[0] = j;

            for (i = 1; i <= n; i++){

                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                d[i] = Math.min(Math.min((d[i-1]+1), p[i]+1), p[i-1]+cost);
            }

            _d = p;
            p = d;
            d = _d;

        }
        return p[n];
    }
}
