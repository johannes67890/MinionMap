package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.MinPQ;

public class MinPQTest {

    MinPQ<Integer> minPQ;
    @BeforeEach
    public void setUp(){
        minPQ = new MinPQ<>(10);
        for(int i = 0; i < 9; i++){
            minPQ.insert((i+1)*2);
        }
    }
    
    @Test
    public void testEmpty(){
        assertNotNull(minPQ);
        assertFalse(minPQ.isEmpty());
    }

    @Test
    public void testSizeAndInsert(){
        assertNotNull(minPQ);
        assertEquals(minPQ.size(), 9);
        minPQ.insert(1);
        assertEquals(minPQ.size(), 10);
    }

    @Test
    public void testDelMin(){
        assertNotNull(minPQ);
        assertEquals(minPQ.delMin(), 2);
        assertEquals(minPQ.size(), 8);
    }

}
