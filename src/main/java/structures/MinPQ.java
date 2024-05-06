package structures;


/**
 * Priorityqueue that works for {@link Comparable} objects
 * 
 * Sorts an array of a type, whereas the minimum value is placed at index 1.
 * index 0 is left empty.
 * 
 */
public class MinPQ<Key extends Comparable<Key>> {

    private Key[] pq;

    private int N = 0;

    /**
     * A constructor for the priority queue, that takes a parameter, which
     * describes the length of the minPQ, as the minPQ uses an array.
     * @param maxN The length of the Priority Queue
     */
    @SuppressWarnings("unchecked")
    public MinPQ(int maxN){
        pq = (Key[]) new Comparable[maxN+1];
    }

    /**
     * Checks wether the number of items in the minPQ is 0. Where we can safely
     * assume that the minPQ is empty.
     * @return true if the minPQ is empty and false if the minPQ is not empty
     */
    public boolean isEmpty(){
        return N == 0;
    }

    /**
     * Returns the amount of items in the minPQ
     * @return the amount of items in the minPQ
     */
    public int size(){
        return N;
    }

    /**
     * Inserts the item into the minPQ and calls swim, to ensure a correct minPQ
     * @param v the item in question that is inserted into the minPQ
     */
    public void insert(Key v){
        pq[++N] = v;
        swim(N);
    }

    /**
     * Finds the minimum value and removes it from the queue.
     * @return The minimum value
     */
    public Key delMin(){
        Key min = pq[1];
        exch(1, N--);
        pq[N+1] = null;
        sink(1);
        return min;
    }

    /**
     * Compares two items of index i and j
     * @param i first items index
     * @param j second items index
     * @return true if i is of higher value than j, otherwise return false.
     */
    private boolean more(int i, int j){
        return pq[i].compareTo(pq[j]) > 0;
    }

    /**
     * Exchange places of two items in the minPQ
     * @param i the first item, index position
     * @param j the seconds item, index position
     */
    private void exch(int i, int j){
        Key t = pq[i]; pq[i] = pq[j]; pq[j] = t;
    }


    private void swim(int k){
        while (k  > 1 && more(k/2, k)){
            exch(k/2, k);
            k = k/2;
        }

    }

    private void sink(int k){
        while (2*k <= N){
            int j = 2*k;
            if (j < N && more(j, j+1)) j++;
            if (!more(k, j)) break;
            exch(k, j);
            k = j;
        }

    }
}
