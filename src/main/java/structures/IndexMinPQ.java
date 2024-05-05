package structures;

import java.util.NoSuchElementException;
import java.util.HashMap;

public class IndexMinPQ<Key extends Comparable<Key>>  {
    private long n;
    private HashMap<Long, Long> qp;
    private HashMap<Long, Long> pq;
    private HashMap<Long, Key> keys;

    /**
     * Initializes an empty indexed priority queue with indices between {@code 0}
     * and {@code maxN - 1}.
     * @param  maxN the keys on this priority queue are index from {@code 0}
     *         {@code maxN - 1}
     * @throws IllegalArgumentException if {@code maxN < 0}
     */
    public IndexMinPQ() {
        n = 0;
        keys = new HashMap<>();
        pq = new HashMap<>();
        qp = new HashMap<>();
        
    }
     /**
     * Returns the key associated with index {@code i}.
     *
     * @param  i the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public Key keyOf(long i) {
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        return keys.get(i);
    }

    public long getFromPq(long i) {
        return pq.get(i);
    }

    public long getFromQp(long i) {
        return qp.get(i);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(long i) {
        return qp.containsKey(i);
    }

    public void insert(long i, Key key) {
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        n++;
        qp.put(i,n);
        pq.put(n, i);
        keys.put(i, key);
        swim(n);
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public long delMin() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");
        long min = pq.get(1L);
        exch(1, n--);
        sink(1);
        assert min == pq.get(n+1);
        qp.remove(min);
        keys.remove(min);
        return min;
    }
    /**
     * Decrease the key associated with index {@code i} to the specified value.
     *
     * @param  i the index of the key to decrease
     * @param  key decrease the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void decreaseKey(long i, Key key) {
        if(!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        if (keys.get(i).compareTo(key) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
        if (keys.get(i).compareTo(key) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
        keys.put(i, key);
        swim(qp.get(i));
    }

    /**
     * 
     * @param i
     * @param j
     * @return
     */
    public boolean greater(long i, long j) {
        return keys.get(pq.get(i)).compareTo(keys.get(pq.get(j))) > 0;
    }

   /***************************************************************************
    * Heap helper functions.
    ***************************************************************************/

    /**
     * Swim helper functions to maintain heap invariant
     * @param k index to swim
     */
    private void swim(long k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    /**
     * Sink helper function to maintain heap invariant
     * @param k
     */
    private void sink(long k) {
        while (2*k <= n) {
            long j = 2*k;
            if (j < n && greater(j, j+1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }
    }
    /**
     * Swap the elements in the priority queue at index i and j
     */
    public void exch(long i, long j) {
        long swap = pq.get(i);
        pq.put(i, pq.get(j));
        pq.put(j, swap);
        qp.put(pq.get(i), i);
        qp.put(pq.get(j), j);
    }
}