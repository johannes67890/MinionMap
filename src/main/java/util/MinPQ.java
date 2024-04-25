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

    public MinPQ(int maxN){
        pq = (Key[]) new Comparable[maxN+1];
    }

    public boolean isEmpty(){
        return N == 0;
    }

    public int size(){
        return N;
    }

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

    private boolean more(int i, int j){
        return pq[i].compareTo(pq[j]) > 0;
    }
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
