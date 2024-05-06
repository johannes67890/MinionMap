package pathfinding;
import org.junit.Test;

import structures.IndexMinPQ;

import static org.junit.Assert.*;

public class IndexMinPQTest {
        
        @Test
        public void testInsertAndDelMin() {
            IndexMinPQ<Integer> pq = new IndexMinPQ<>();
            
            pq.insert(1, 10);
            pq.insert(2, 5);
            pq.insert(3, 15);
            
            assertEquals(2, pq.delMin());
            assertEquals(1, pq.delMin());
            assertEquals(3, pq.delMin());
        }
        
        @Test
        public void testDecreaseKey() {
            IndexMinPQ<Integer> pq = new IndexMinPQ<>();
            
            pq.insert(1, 10);
            pq.insert(2, 5);
            pq.insert(3, 15);
            
            pq.decreaseKey(3, 3);
            pq.decreaseKey(1, 2);
            
            assertEquals(1, pq.delMin());
            assertEquals(3, pq.delMin());
            assertEquals(2, pq.delMin());
        }
        
        @Test
        public void testContains() {
            IndexMinPQ<Integer> pq = new IndexMinPQ<>();
            
            pq.insert(1, 10);
            pq.insert(2, 5);
            pq.insert(3, 15);
            
            assertTrue(pq.contains(1));
            assertTrue(pq.contains(2));
            assertTrue(pq.contains(3));
            assertFalse(pq.contains(4));
        }

        @Test
        public void testGreater() {
            IndexMinPQ<Double> pq = new IndexMinPQ<>();
            pq.insert(3, 5.6);
            pq.insert(4, 7.6);
    
            assertTrue(pq.greater(2, 1));
            assertFalse(pq.greater(1, 2));
            assertFalse(pq.greater(1, 1));
        }

        /**
         * Test case for the exch() method of the IndexMinPQ class.
         */
        @Test
        public void testExch() {
            IndexMinPQ<Double> pq = new IndexMinPQ<>();
            pq.insert(3, 5.6);
            pq.insert(4, 7.6);

            pq.exch(1,2);

            assertEquals(4, pq.getFromPq(1));
            
            assertEquals(1, pq.getFromQp(4));

            assertEquals(3, pq.getFromPq(2));

            assertEquals(2, pq.getFromQp(3));    
        }
}
