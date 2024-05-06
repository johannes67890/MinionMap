package structures;

import java.util.NoSuchElementException;
import java.util.HashMap;

// Imports for docs
import parser.Tag;
import pathfinding.Dijkstra;

/**
 * <p>
 *  <b>IMPORTANT NOTE</b>: This class is made from the <a href="https://algs4.cs.princeton.edu/">Princeton University Algorithms Library</a>.
 * </p>
 *  The {@code IndexMinPQ} class represents an indexed priority queue of generic keys.
 *  It supports the <em>insert</em> and <em>delete-the-minimum</em>
 *  operations, along with <em>delete</em> and <em>change-the-key</em>
 *  methods. It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a binary heap along with a {@link HashMap} with the key as the {@code id} of a {@link Tag} and 
 *  the value is a {@code id} for a {@link Tag} in the {@code distTo} field, used in {@link Dijkstra}.
 * 
 *  The <em>insert</em>, <em>delete-the-minimum</em>, <em>delete</em>,
 *  <em>change-key</em>, <em>decrease-key</em>, and <em>increase-key</em>
 *  operations take &Theta;(log <em>n</em>) time in the worst case,
 *  where <em>n</em> is the number of elements in the priority queue.
 *  Construction takes time proportional to the specified capacity.
 *  <p>
 *  <p>
 *     The Key is the generic type of key on this priority queue. That is, the key is the distance between two nodes in the graph.
 *  </p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  @see {@link Dijkstra} The algorithm that uses this Priority Queue
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 * 
 *  @param <Key> the generic type of key on this priority queue
 */
public class IndexMinPQ<Key extends Comparable<Key>>  {
    private long n;                  // number of elements on PQ
    private HashMap<Long, Long> qp;  // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private HashMap<Long, Long> pq;  // binary heap using 1-based indexing
    private HashMap<Long, Key> keys; // keys[i] = priority of i

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

    /**
     * @param i the index of the key to return
     * @return the index associated with the minimum key in the priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public long getFromPq(long i) {
        return pq.get(i);
    }

    /**
     * @param i the index of the key to return
     * @return the index associated with the minimum key in the inverse priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public long getFromQp(long i) {
        return qp.get(i);
    }

    /**
     * @return if the priority queue is empty
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * @param i the index of the key to return
     * @return if the priority queue contains the index
     */
    public boolean contains(long i) {
        return qp.containsKey(i);
    }

    /**
     * Inserts an index with a key.
     * @param i index
     * @param key key
     */
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
     * Check if the key associated with index {@code i} is greater than the key associated with index {@code j}
     * @param i index
     * @param j index
     * @return true if the key associated with index {@code i} is greater than the key associated with index {@code j}
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