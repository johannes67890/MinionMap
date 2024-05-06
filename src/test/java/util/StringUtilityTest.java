package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StringUtilityTest {

    @Test
    void testGetLevenhsteinDistance(){
        String str1 = "Andreas";
        String str2 = "Andrees";

        assertEquals(1, StringUtility.getLevenshteinDistance(str1, str2));

        str2 = "And";

        assertEquals(4, StringUtility.getLevenshteinDistance(str1, str2));
    }

    @Test
    void testFormatString(){
        String str = "hejsa_med_dejsa";
        assertEquals("Hejsa Med Dejsa", StringUtility.formatString(str));
    }

}
