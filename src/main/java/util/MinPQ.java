package util;


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
     * Constructor for the MinPQ
     * @param maxN The maximum number of elements in the queue
     */
    public MinPQ(int maxN){
        pq = (Key[]) new Comparable[maxN+1];
    }

    /**
     * Checks if the queue is empty
     * @return true if the queue is empty, false if not.
     */
    public boolean isEmpty(){
        return N == 0;
    }

    /**
     * Returns the size of the queue
     * @return the size of the queue
     */
    public int size(){
        return N;
    }

    /**
     * Inserts a value into the queue
     * @param v The value to insert
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
     * Checks if the value at index i is bigger than the value at index j
     * @param i 
     * @param j
     * @return true if the value at index i is bigger than the value at index j, false if not.
     */
    private boolean more(int i, int j){
        return pq[i].compareTo(pq[j]) > 0;
    }

    /**
     * Exchanges the values at index i and j
     * @param i
     * @param j
     */
    private void exch(int i, int j){
        Key t = pq[i]; pq[i] = pq[j]; pq[j] = t;
    }

    /**
     * Swims the value at index k up the queue
     * @param k 
     */
    private void swim(int k){
        while (k  > 1 && more(k/2, k)){
            exch(k/2, k);
            k = k/2;
        }

    }

    /**
     * Sinks the value at index k down the queue
     * @param k
     */
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
